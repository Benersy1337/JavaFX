package main;

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

public class Main extends Application {

    private List<Conta> contas = new ArrayList<>();
    private ComboBox<String> contaComboBox;
    private TextField valorTextField;
    private Button voltarButton;
    private ObservableList<String> observableTitulares;
    private Label saldoLabel; 

    private VBox tela1;
    private VBox tela2;

    public Conta novaConta;

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

        Button criarContaButton = new Button("Criar Conta");
        criarContaButton.setOnAction(e -> criarNovaConta(nomeTitularTextField.getText(), tipoContaComboBox.getValue()));

        Button listarContasButton = new Button("Listar Contas");
        listarContasButton.setOnAction(e -> listarContas());

        tela1.getChildren().addAll(tipoContaComboBox, nomeTitularTextField, criarContaButton, listarContasButton);

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
        voltarButton.setOnAction(e -> {
            telaCriarContas();
            nomeTitularTextField.clear();
        });
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

    private void criarNovaConta(String nomeTitular, String tipoContaSelecionado) {

        if (nomeTitular.isEmpty()) {
            exibirAlerta("Preencha o campo nome do titular.");
            return;
        }

        if ("Conta Corrente".equals(tipoContaSelecionado)) {
            novaConta = new ContaCorrente(nomeTitular, 2000);
        } else if ("Conta Poupança".equals(tipoContaSelecionado)) {
            novaConta = new ContaPoupanca(nomeTitular);
        }

        if (novaConta != null) {
            contas.add(novaConta);
            listarContas();
        }

    }

    private void atualizarSaldoTela2() {
        int selectedIndex = contaComboBox.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            Conta contaSelecionada = contas.get(selectedIndex);
            saldoLabel.setText("Saldo: " + contaSelecionada.getSaldo());
        } else {
            saldoLabel.setText("Saldo: ");
        }
    }

    private void listarContas() {

        if(novaConta == null){
            exibirAlerta("Não há nenhuma conta cadastrada.");
        } else {
            observableTitulares = FXCollections.observableArrayList(
                contas.stream()
                        .map(Conta::getTitular)
                        .collect(Collectors.toList())
            );
        
            contaComboBox.setItems(observableTitulares);
            contaComboBox.getSelectionModel();

            valorTextField.clear();

            // Adicione um evento para ouvir a seleção na ComboBox e atualizar o saldo
            contaComboBox.setOnAction(e -> atualizarSaldoTela2());

            // // Atualize o saldo na tela 2 com base na conta selecionada
            atualizarSaldoTela2();
            
            tela2.setVisible(true);
            tela1.setVisible(false);
            voltarButton.setVisible(true);

        }

    }

    
    private void depositarNaContaSelecionada() {
        int selectedIndex = contaComboBox.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            Conta contaSelecionada = contas.get(selectedIndex);
            try {
                double valor = Double.parseDouble(valorTextField.getText());
                contaSelecionada.depositar(valor);
                listarContas();
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
                listarContas();
            } catch (NumberFormatException e) {
                exibirAlerta("Valor inválido. Por favor, insira um valor numérico.");
            }
            
            // Atualize o saldoLabel com o saldo da conta selecionada
            saldoLabel.setText("Saldo: " + contaSelecionada.getSaldo());
        }
    }

    private void telaCriarContas() {
        tela1.setVisible(true);
        tela2.setVisible(false);
        voltarButton.setVisible(false);
    }

    public void exibirAlerta(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}