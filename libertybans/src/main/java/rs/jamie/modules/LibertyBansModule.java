package rs.jamie.modules;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;
import rs.jamie.PunishDispatch;
import rs.jamie.PunishType;
import space.arim.libertybans.api.*;
import space.arim.libertybans.api.event.PardonEvent;
import space.arim.libertybans.api.event.PostPunishEvent;
import space.arim.libertybans.api.punish.Punishment;
import space.arim.omnibus.Omnibus;
import space.arim.omnibus.OmnibusProvider;
import space.arim.omnibus.events.EventConsumer;
import space.arim.omnibus.events.ListenerPriorities;
import space.arim.omnibus.util.concurrent.ReactionStage;

import java.net.InetAddress;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class LibertyBansModule implements Module {

    private final PunishDispatch punishDispatch;
    private final Omnibus omnibus;
    private final LibertyBans libertyBans;

    public LibertyBansModule(PunishDispatch punishDispatch) {
        this.punishDispatch = punishDispatch;
        omnibus = OmnibusProvider.getOmnibus();
        libertyBans = omnibus.getRegistry().getProvider(LibertyBans.class).orElseThrow();
    }

    @Override
    public void execute(Plugin plugin) {
        EventConsumer<PostPunishEvent> punishListener = event -> {
            Punishment punishment = event.getPunishment();
            PunishType punishType = switch (punishment.getType()) {
                case BAN -> PunishType.BAN;
                case MUTE -> PunishType.MUTE;
                case WARN -> PunishType.WARN;
                case KICK -> PunishType.KICK;
            };
            boolean ipbased = false;
            InetAddress punished_address = null;
            UUID punished = null;
            UUID punisher = null;
            switch (punishment.getVictim().getType()) {
                case PLAYER -> {
                    PlayerVictim playerVictim = (PlayerVictim) punishment.getVictim();
                    punished = playerVictim.getUUID();
                }
                case COMPOSITE -> {
                    CompositeVictim compositeVictim = (CompositeVictim) punishment.getVictim();
                    punished = compositeVictim.getUUID();
                    try {
                        punished_address = compositeVictim.getAddress().toInetAddress();
                    } catch (Exception ignored){}
                    ipbased = true;
                }
                case ADDRESS -> {
                    AddressVictim addressVictim = (AddressVictim) punishment.getVictim();
                    try {
                        punished_address = addressVictim.getAddress().toInetAddress();
                    } catch (Exception ignored){}

                    ipbased = true;
                }
            }

            switch (punishment.getOperator().getType()) {
                case PLAYER -> {
                    PlayerOperator playerOperator = (PlayerOperator) punishment.getOperator();
                    punisher = playerOperator.getUUID();
                }
                case CONSOLE -> punisher = UUID.fromString("f78a4d8d-d51b-4b39-98a3-230f2de0c670");
            }
            Long end = null;
            if(punishment.getEndDate()!=null) {
                end = punishment.getEndDateSeconds();
            }

            rs.jamie.Punishment p = new rs.jamie.Punishment(false, punishType, punished, punished_address, punisher, punishment.getReason(), end, null, ipbased);
            punishDispatch.dispatchEvent(new rs.jamie.PunishEvent(p));
        };

        EventConsumer<PardonEvent> pardonListener = event -> {
            PunishType punishType = PunishType.NULL;
            InetAddress punished_address = null;
            UUID punished = null;
            UUID punisher = null;
            boolean ipbased = false;
            switch (event.getPardonedVictim().getType()) {
                case PLAYER -> {
                    PlayerVictim playerVictim = (PlayerVictim) event.getPardonedVictim();
                    punished = playerVictim.getUUID();
                }
                case COMPOSITE -> {
                    CompositeVictim compositeVictim = (CompositeVictim) event.getPardonedVictim();
                    punished = compositeVictim.getUUID();
                    try {
                        punished_address = compositeVictim.getAddress().toInetAddress();
                    } catch (Exception ignored){}
                    ipbased = true;
                }
                case ADDRESS -> {
                    AddressVictim addressVictim = (AddressVictim) event.getPardonedVictim();
                    try {
                        punished_address = addressVictim.getAddress().toInetAddress();
                    } catch (Exception ignored){}
                    ipbased = true;
                }
            }

            switch (event.getOperator().getType()) {
                case PLAYER -> {
                    PlayerOperator playerOperator = (PlayerOperator) event.getOperator();
                    punisher = playerOperator.getUUID();
                }
                case CONSOLE -> punisher = UUID.fromString("f78a4d8d-d51b-4b39-98a3-230f2de0c670");
            }

            rs.jamie.Punishment p = new rs.jamie.Punishment(true, punishType, punished, punished_address, punisher, null, null, null, ipbased);
            punishDispatch.dispatchEvent(new rs.jamie.PunishEvent(p));
        };

        omnibus.getEventBus().registerListener(PostPunishEvent.class, ListenerPriorities.HIGHEST, punishListener);
        omnibus.getEventBus().registerListener(PardonEvent.class, ListenerPriorities.HIGHEST, pardonListener);
    }

    private CompletableFuture<Boolean> hasPunishment(UUID uuid, @Nullable InetAddress inetaddress, PunishmentType punishmentType) {
        NetworkAddress address = null;
        if(inetaddress!=null) {
            address = NetworkAddress.of(inetaddress);
        }
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        ReactionStage<Optional<Punishment>> punishments =  libertyBans.getSelector()
                .selectionByApplicabilityBuilder(uuid, address)
                .type(punishmentType)
                .build()
                .getFirstSpecificPunishment();

        punishments.thenAccept(punishment -> {
            boolean muted = punishment
                    .map(p -> !p.isExpired())
                    .orElse(false);
            future.complete(muted);
        });
        return future;
    }

    @Override
    public CompletableFuture<Boolean> isMuted(UUID uuid, @Nullable InetAddress address) {
        return hasPunishment(uuid, address, PunishmentType.MUTE);
    }

    @Override
    public CompletableFuture<Boolean> isBanned(UUID uuid, @Nullable InetAddress address) {
        return hasPunishment(uuid, address, PunishmentType.BAN);
    }

}
