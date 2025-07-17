package se233.chapter2.controller;

import se233.chapter2.Launcher;
import se233.chapter2.model.Currency;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.ArrayList;

class WatchTask implements Callable<Void> {
    @Override
    public Void call() {
        List<Currency> allCurrency = Launcher.getCurrencyList();
        List<String> belowThreshold = new ArrayList<>();
        List<String> aboveThreshold = new ArrayList<>();

        for(Currency currency : allCurrency) {
            if (currency.getWatch() && currency.getWatchRate() != 0 && currency.getCurrent() != null) {
                double currentRate = currency.getCurrent().getRate();
                double watchRate = currency.getWatchRate();

                if (currentRate < watchRate) {
                    belowThreshold.add(String.format("%s (%.4f < %.4f)",
                            currency.getShortCode(), currentRate, watchRate));
                }
            }
        }
        if (!belowThreshold.isEmpty()) {
            showAlert("Currency Watch Alert - Below Threshold",
                    "The following currencies have dropped below your watch rate:",
                    belowThreshold, Alert.AlertType.WARNING);
        }

        if (!aboveThreshold.isEmpty()) {
            showAlert("Currency Watch Alert - Above Threshold",
                    "The following currencies have risen above your watch rate:",
                    aboveThreshold, Alert.AlertType.INFORMATION);
        }
        return null;
    }

    private void showAlert(String title, String header, List<String> currencies, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);

        StringBuilder content = new StringBuilder();
        for (int i = 0; i < currencies.size(); i++) {
            content.append(currencies.get(i));
            if (i < currencies.size() - 1) {
                content.append("\n");
            }
        }

        alert.setContentText(content.toString());

        alert.setResizable(true);
        alert.getDialogPane().setPrefSize(400, 200);

        if (alertType == Alert.AlertType.WARNING) {
            ButtonType acknowledgeButton = new ButtonType("Okay");
            alert.getButtonTypes().setAll(acknowledgeButton);
        }

        alert.showAndWait();
    }
}