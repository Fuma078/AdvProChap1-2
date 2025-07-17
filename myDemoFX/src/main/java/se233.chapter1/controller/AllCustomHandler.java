package se233.chapter1.controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import se233.chapter1.Launcher;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.input.ClipboardContent;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import java.util.ArrayList;
import se233.chapter1.model.character.BasedCharacter;
import se233.chapter1.model.item.BasedEquipment;
import se233.chapter1.model.item.Weapon;
import se233.chapter1.model.item.Armor;

public class AllCustomHandler {
    private static BasedEquipment replacedItem = null;

    public static class GenCharacterHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            unequipAllItems();
            Launcher.setMainCharacter(GenCharacter.setUpCharacter());
            Launcher.refreshPane();
        }
        private static void unequipAllItems() {
            Launcher.setEquippedWeapon(null);
            Launcher.setEquippedArmor(null);
        }
    }

    public static class UnequipWeaponHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            Weapon currentWeapon = Launcher.getEquippedWeapon();
            if (currentWeapon != null) {

                Launcher.setEquippedWeapon(null);
                BasedCharacter character = Launcher.getMainCharacter();
                character.equipWeapon(null);

                character.setPower(character.getBasedPow());
                Launcher.setMainCharacter(character);

                Launcher.refreshPane();

                System.out.println("Unequipped weapon: " + currentWeapon.getName());
            }
        }
    }

    public static class UnequipArmorHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            Armor currentArmor = Launcher.getEquippedArmor();
            if (currentArmor != null) {
                Launcher.setEquippedArmor(null);
                BasedCharacter character = Launcher.getMainCharacter();
                character.equipArmor(null);

                character.setDefense(character.getBasedDef());
                character.setResistance(character.getBasedRes());
                Launcher.setMainCharacter(character);

                Launcher.refreshPane();

                System.out.println("Unequipped armor: " + currentArmor.getName());
            }
        }
    }

    public static class UnequipAllItemsHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            Weapon currentWeapon = Launcher.getEquippedWeapon();
            Armor currentArmor = Launcher.getEquippedArmor();
            BasedCharacter character = Launcher.getMainCharacter();

            if (currentWeapon != null) {
                Launcher.setEquippedWeapon(null);
                character.equipWeapon(null);
                character.setPower(character.getBasedPow());
                System.out.println("Unequipped weapon: " + currentWeapon.getName());
            }

            if (currentArmor != null) {
                Launcher.setEquippedArmor(null);
                character.equipArmor(null);
                character.setDefense(character.getBasedDef());
                character.setResistance(character.getBasedRes());
                System.out.println("Unequipped armor: " + currentArmor.getName());
            }

            Launcher.setMainCharacter(character);
            Launcher.refreshPane();

            if (currentWeapon != null || currentArmor != null) {
                System.out.println("All items unequipped successfully!");
            } else {
                System.out.println("No items were equipped to unequip.");
            }
        }
    }

    public static void onDragDetected(MouseEvent event, BasedEquipment equipment, ImageView imgView) {
        Dragboard db = imgView.startDragAndDrop(TransferMode.ANY);
        db.setDragView(imgView.getImage());
        ClipboardContent content = new ClipboardContent();
        content.put(equipment.DATA_FORMAT, equipment);
        db.setContent(content);
        event.consume();
    }

    public static void onDragOver(DragEvent event, String type) {
        Dragboard dragboard = event.getDragboard();
        BasedEquipment retrievedEquipment = (BasedEquipment)dragboard.getContent(
                BasedEquipment.DATA_FORMAT);

        if (dragboard.hasContent(BasedEquipment.DATA_FORMAT) &&
                retrievedEquipment.getClass().getSimpleName().equals(type)) {

            BasedCharacter character = Launcher.getMainCharacter();
            if (canEquipItem(character, retrievedEquipment)) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
        }
    }

    public static void onDragDropped(DragEvent event, Label lbl, StackPane imgGroup) {
        boolean dragCompleted = false;
        Dragboard dragboard = event.getDragboard();
        ArrayList<BasedEquipment> allEquipments = Launcher.getAllEquipments();
        replacedItem = null;

        if(dragboard.hasContent(BasedEquipment.DATA_FORMAT)) {
            BasedEquipment retrievedEquipment = (BasedEquipment)dragboard.getContent(
                    BasedEquipment.DATA_FORMAT);
            BasedCharacter character = Launcher.getMainCharacter();

            if (!canEquipItem(character, retrievedEquipment)) {

                System.out.println("Cannot equip " + retrievedEquipment.getName() +
                        " - incompatible with " + character.getName() +
                        " (Character type: " + character.getType() + ")");
                event.setDropCompleted(false);
                return;
            }

            if(retrievedEquipment.getClass().getSimpleName().equals("Weapon")) {
                if (Launcher.getEquippedWeapon() != null) {
                    replacedItem = Launcher.getEquippedWeapon();
                    allEquipments.add(Launcher.getEquippedWeapon());
                }
                Launcher.setEquippedWeapon((Weapon) retrievedEquipment);
                character.equipWeapon((Weapon) retrievedEquipment);
            } else if(retrievedEquipment.getClass().getSimpleName().equals("Armor")) {
                if (Launcher.getEquippedArmor() != null) {
                    replacedItem = Launcher.getEquippedArmor();
                    allEquipments.add(Launcher.getEquippedArmor());
                }
                Launcher.setEquippedArmor((Armor) retrievedEquipment);
                character.equipArmor((Armor) retrievedEquipment);
            }

            Launcher.setMainCharacter(character);
            Launcher.setAllEquipments(allEquipments);

            ImageView imgView = new ImageView();
            if (imgGroup.getChildren().size()!=1) {
                imgGroup.getChildren().remove(1);
            }
            lbl.setText(retrievedEquipment.getClass().getSimpleName() + ":\n" +
                    retrievedEquipment.getName());

            try {
                String imagePath = retrievedEquipment.getImagepath();
                Image image = new Image(AllCustomHandler.class.getClassLoader().getResourceAsStream(imagePath));
                imgView.setImage(image);
            } catch (Exception e) {
                System.err.println("Could not load equipment image: " + retrievedEquipment.getImagepath());
            }

            imgGroup.getChildren().add(imgView);
            dragCompleted = true;
        }
        event.setDropCompleted(dragCompleted);
    }

    private static boolean canEquipItem(BasedCharacter character, BasedEquipment equipment) {
        boolean isBattleMage = character.getClass().getSimpleName().equals("BattleMageCharacter");

        if (equipment instanceof Weapon) {
            Weapon weapon = (Weapon) equipment;

            if (isBattleMage) {
                return true;
            }

            return character.getType() == weapon.getDamageType();

        } else if (equipment instanceof Armor) {
            return true;
        }
        return false;
    }

    public static void onEquipDone(DragEvent event) {
        Dragboard dragboard = event.getDragboard();
        ArrayList<BasedEquipment> allEquipments = Launcher.getAllEquipments();
        BasedEquipment retrievedEquipment = (BasedEquipment)dragboard.getContent(
                BasedEquipment.DATA_FORMAT);

        if (event.isDropCompleted()) {
            int pos = -1;
            for(int i = 0; i < allEquipments.size(); i++) {
                if (allEquipments.get(i).getName().equals(retrievedEquipment.getName())) {
                    pos = i;
                    break;
                }
            }
            if (pos != -1) {
                allEquipments.remove(pos);
            }
        } else {
            if (replacedItem != null) {
                for (int i = allEquipments.size() - 1; i >= 0; i--) {
                    if (allEquipments.get(i).getName().equals(replacedItem.getName())) {
                        allEquipments.remove(i);
                        break;
                    }
                }
            }

            boolean itemInInventory = false;
            for (BasedEquipment equipment : allEquipments) {
                if (equipment.getName().equals(retrievedEquipment.getName())) {
                    itemInInventory = true;
                    break;
                }
            }

            if (!itemInInventory) {
                allEquipments.add(retrievedEquipment);
            }
        }

        replacedItem = null;
        Launcher.setAllEquipments(allEquipments);
        Launcher.refreshPane();
    }

    public static void onInvalidDragOver(DragEvent event) {
    }

    public static void onInvalidDragDropped(DragEvent event) {
        event.setDropCompleted(false);
    }
}