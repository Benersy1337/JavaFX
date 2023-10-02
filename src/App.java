import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.geometry.Insets;
import java.util.List;
import java.util.ArrayList;
import javafx.scene.control.ComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import java.util.stream.Collectors;

import banco.*;

public class App extends Application {

    private List<Conta> contas = new ArrayList<>();
    private ComboBox<String> contaComboBox;
    private TextField valorTextField;
    private Button voltarButton;
    private ObservableList<String> observableTitulares;
    private Label saldoLabel; 

    private VBox tela1;
    private VBox tela2;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Banco Virtual");

        tela1 = new VBox();
        tela1.setSpacing(10);
        tela1.setPadding(new Insets(10));

        ComboBox<String> tipoContaComboBox = new ComboBox<>();
        tipoContaComboBox.getItems().addAll("Conta Corrente", "Conta Poupança");
        tipoContaComboBox.setValue("Conta Corrente");

        TextField nomeTitularTextField = new TextField();
        nomeTitularTextField.setPromptText("Nome do Titular");

        TextField saldoInicialTextField = new TextField();
        saldoInicialTextField.setPromptText("Saldo Inicial");

        Button criarContaButton = new Button("Criar Conta");
        criarContaButton.setOnAction(e -> criarNovaConta(nomeTitularTextField.getText(), saldoInicialTextField.getText(), tipoContaComboBox.getValue()));

        Button listarContasButton = new Button("Listar Contas");
        listarContasButton.setOnAction(e -> mostrarTelaListarContas());

        tela1.getChildren().addAll(tipoContaComboBox, nomeTitularTextField, saldoInicialTextField, criarContaButton, listarContasButton);

        tela2 = new VBox();
        tela2.setSpacing(10);
        tela2.setPadding(new Insets(10));

        contaComboBox = new ComboBox<>();
        valorTextField = new TextField();
        valorTextField.setPromptText("Valor");

        Button depositarButton = new Button("Depositar");
        depositarButton.setOnAction(e -> depositarNaContaSelecionada());

        Button sacarButton = new Button("Sacar");
        sacarButton.setOnAction(e -> sacarDaContaSelecionada());

        voltarButton = new Button("Voltar");
        voltarButton.setOnAction(e -> mostrarTelaCriarListarContas());
        voltarButton.setVisible(false);

        saldoLabel = new Label("Saldo: "); 
        tela2.getChildren().addAll(contaComboBox, valorTextField, depositarButton, sacarButton, voltarButton, saldoLabel);

        tela2.setVisible(false);

        VBox root = new VBox();
        root.getChildren().addAll(tela1, tela2);

        Scene scene = new Scene(root, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void criarNovaConta(String nomeTitular, String saldoInicialStr, String tipoContaSelecionado) {
        if (nomeTitular.isEmpty() || saldoInicialStr.isEmpty()) {
            exibirAlerta("Preencha todos os campos.");
            return;
        }

        double saldoInicial;
        try {
            saldoInicial = Double.parseDouble(saldoInicialStr);
        } catch (NumberFormatException e) {
            exibirAlerta("Saldo Inicial inválido. Insira um valor numérico.");
            return;
        }

        Conta novaConta = null;
        if ("Conta Corrente".equals(tipoContaSelecionado)) {
            novaConta = new ContaCorrente(nomeTitular, saldoInicial);
        } else if ("Conta Poupança".equals(tipoContaSelecionado)) {
            novaConta = new ContaPoupanca(nomeTitular);
        }

        if (novaConta != null) {
            contas.add(novaConta);
            mostrarTelaListarContas();
        }
    }

    private void mostrarTelaListarContas() {
        observableTitulares = FXCollections.observableArrayList(
                contas.stream()
                      .map(Conta::getTitular)
                      .collect(Collectors.toList())
        );
        contaComboBox.setItems(observableTitulares);
        contaComboBox.getSelectionModel().selectFirst();
        valorTextField.clear();
        
        int selectedIndex = contaComboBox.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            Conta contaSelecionada = contas.get(selectedIndex);
            saldoLabel.setText("Saldo: " + contaSelecionada.getSaldo());
        } else {
            saldoLabel.setText("Saldo: " );
        }
        
        tela2.setVisible(true);
        tela1.setVisible(false);
        voltarButton.setVisible(true);
    }

    private void depositarNaContaSelecionada() {
        int selectedIndex = contaComboBox.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            Conta contaSelecionada = contas.get(selectedIndex);
            try {
                double valor = Double.parseDouble(valorTextField.getText());
                contaSelecionada.depositar(valor);
                mostrarTelaListarContas();
            } catch (NumberFormatException e) {
                exibirAlerta("Valor inválido. Por favor, insira um valor numérico.");
            }
            
            // Atualize o saldoLabel com o saldo da conta selecionada
            saldoLabel.setText("Saldo: " + contaSelecionada.getSaldo());
        }
    }

    private void sacarDaContaSelecionada() {
        int selectedIndex = contaComboBox.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            Conta contaSelecionada = contas.get(selectedIndex);
            try {
                double valor = Double.parseDouble(valorTextField.getText());
                contaSelecionada.sacar(valor);
                mostrarTelaListarContas();
            } catch (NumberFormatException e) {
                exibirAlerta("Valor inválido. Por favor, insira um valor numérico.");
            }
            
            // Atualize o saldoLabel com o saldo da conta selecionada
            saldoLabel.setText("Saldo: " + contaSelecionada.getSaldo());
        }
    }

    private void mostrarTelaCriarListarContas() {
        tela1.setVisible(true);
        tela2.setVisible(false);
        voltarButton.setVisible(false);
    }

    private void exibirAlerta(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}