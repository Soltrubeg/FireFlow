package de.blazemcworld.fireflow.code.node.impl.player.inventory;

import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.type.ItemType;
import de.blazemcworld.fireflow.code.type.ListType;
import de.blazemcworld.fireflow.code.type.PlayerType;
import de.blazemcworld.fireflow.code.value.ListValue;
import de.blazemcworld.fireflow.code.value.PlayerValue;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class GetPlayerEquipmentNode extends Node {

    public GetPlayerEquipmentNode() {
        super("get_player_equipment", "Get Player Equipment", "Gets a list of the player's equipment.", Material.IRON_CHESTPLATE);

        Input<PlayerValue> player = new Input<>("player", "Player", PlayerType.INSTANCE);
        Output<ListValue<ItemStack>> main = new Output<>("main", "Main", ListType.of(ItemType.INSTANCE));

        main.valueFrom((ctx) -> player.getValue(ctx).tryGet(ctx, p ->
                new ListValue<>(ItemType.INSTANCE, List.of(
                        p.getEquipment().getHelmet().clone(),
                        p.getEquipment().getChestplate().clone(),
                        p.getEquipment().getLeggings().clone(),
                        p.getEquipment().getBoots().clone()
                )), new ListValue<>(ItemType.INSTANCE)
        ));
    }

    @Override
    public Node copy() {
        return new GetPlayerEquipmentNode();
    }

}
