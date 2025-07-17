package se233.chapter1.view;

import javafx.scene.control.ScrollPane;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.event.EventHandler;
import javafx.scene.input.DragEvent;
import se233.chapter1.Launcher;
import se233.chapter1.model.item.Weapon;
import se233.chapter1.model.item.Armor;
import se233.chapter1.controller.AllCustomHandler;
import static se233.chapter1.controller.AllCustomHandler.onDragOver;
import static se233.chapter1.controller.AllCustomHandler.onDragDropped;

public class EquipPane extends ScrollPane {
    private Weapon equippedWeapon;
    private Armor equippedArmor;

    public EquipPane() {}

    private Pane getDetailsPane() {
        Pane equipmentInfoPane = new VBox(10);
        equipmentInfoPane.setBorder(null);
        ((VBox) equipmentInfoPane).setAlignment(Pos.CENTER);
        equipmentInfoPane.setPadding(new Insets(25, 25, 25, 25));

        Label weaponLbl, armorLbl;
        StackPane weaponImgGroup = new StackPane();
        StackPane armorImgGroup = new StackPane();
        ImageView bg1 = new ImageView();
        ImageView bg2 = new ImageView();
        ImageView weaponImg = new ImageView();
        ImageView armorImg = new ImageView();

        try {
            Image blankImage = new Image(getClass().getClassLoader().getResourceAsStream("se233.chapter1/assets/blank.png"));
            bg1.setImage(blankImage);
            bg2.setImage(blankImage);
        } catch (Exception e) {
            System.err.println("Could not load blank.png: " + e.getMessage());
        }

        weaponImgGroup.getChildren().add(bg1);
        armorImgGroup.getChildren().add(bg2);

        VBox weaponSection = new VBox(5);
        weaponSection.setAlignment(Pos.CENTER);

        if (equippedWeapon != null) {
            weaponLbl = new Label("Weapon:\n" + equippedWeapon.getName());
            try {
                Image weaponImage = new Image(getClass().getClassLoader().getResourceAsStream(equippedWeapon.getImagepath()));
                weaponImg.setImage(weaponImage);
                weaponImgGroup.getChildren().add(weaponImg);
            } catch (Exception e) {
                System.err.println("Could not load weapon image: " + equippedWeapon.getImagepath());
            }
        } else {
            weaponLbl = new Label("Weapon:");
            try {
                Image blankImage = new Image(getClass().getClassLoader().getResourceAsStream("se233.chapter1/assets/blank.png"));
                weaponImg.setImage(blankImage);
            } catch (Exception e) {
                System.err.println("Could not load blank image for weapon");
            }
        }

        weaponSection.getChildren().addAll(weaponLbl, weaponImgGroup);

        VBox armorSection = new VBox(5);
        armorSection.setAlignment(Pos.CENTER);

        if (equippedArmor != null) {
            armorLbl = new Label("Armor: \n" + equippedArmor.getName());
            try {
                Image armorImage = new Image(getClass().getClassLoader().getResourceAsStream(equippedArmor.getImagepath()));
                armorImg.setImage(armorImage);
                armorImgGroup.getChildren().add(armorImg);
            } catch (Exception e) {
                System.err.println("Could not load armor image: " + equippedArmor.getImagepath());
            }
        } else {
            armorLbl = new Label("Armor:");
            try {
                Image blankImage = new Image(getClass().getClassLoader().getResourceAsStream("se233.chapter1/assets/blank.png"));
                armorImg.setImage(blankImage);
            } catch (Exception e) {
                System.err.println("Could not load blank image for armor");
            }
        }

        armorSection.getChildren().addAll(armorLbl, armorImgGroup);

        Button unequipAllBtn = new Button("Unequip All Items");
        unequipAllBtn.setOnAction(new AllCustomHandler.UnequipAllItemsHandler());
        unequipAllBtn.setDisable(equippedWeapon == null && equippedArmor == null);
        unequipAllBtn.setMaxWidth(Double.MAX_VALUE);
        unequipAllBtn.setStyle("-fx-background-color: #ff6b6b; -fx-text-fill: white; -fx-font-weight: bold;");

        weaponImgGroup.setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent e) { onDragOver(e,"Weapon"); }
        });
        armorImgGroup.setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent e) { onDragOver(e, "Armor"); }
        });
        weaponImgGroup.setOnDragDropped(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent e) { onDragDropped(e, weaponLbl, weaponImgGroup); }
        });
        armorImgGroup.setOnDragDropped(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent e) { onDragDropped(e, armorLbl, armorImgGroup); }
        });

        equipmentInfoPane.getChildren().addAll(weaponSection, armorSection, unequipAllBtn);
        return equipmentInfoPane;
    }

    public void drawPane(Weapon equippedWeapon, Armor equippedArmor) {
        this.equippedWeapon = equippedWeapon;
        this.equippedArmor = equippedArmor;
        Pane equipmentInfo = getDetailsPane();
        this.setStyle("-fx-background-color:Red;");
        this.setContent(equipmentInfo);
    }
}