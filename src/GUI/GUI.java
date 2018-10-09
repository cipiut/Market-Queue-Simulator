package GUI;

import Simulare.Panou;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


public class GUI extends Application implements EventHandler<ActionEvent> {
    private TextField nrCase,servMin,servMax,arivMin,arivMax,simulare;
    private Button run;
    private Canvas canvas;
    public static final TextArea err =new TextArea();
    private ChoiceBox span , strategy;
    public static int SPEED;
    private Panou panou;

    private void initValues(){//pentru initializarea variabilelor
        canvas=new Canvas(510,270);//aici va fi grafica
        strategy = new ChoiceBox();
        strategy.getItems().addAll("Random", "Min Clienti", "Min Wait");
        strategy.setValue("Random");
        span= new ChoiceBox();
        span.getItems().addAll("2x", "4x", "8x");
        span.setValue("2x");
        run = new Button("RUN");
        nrCase =new TextField();
        nrCase.setPrefColumnCount(4);
        nrCase.setPromptText("max 10");
        servMin =new TextField();
        servMin.setPrefColumnCount(4);
        servMax =new TextField();
        servMax.setPrefColumnCount(4);
        arivMin =new TextField();
        arivMin.setPrefColumnCount(4);
        arivMax =new TextField();
        arivMax.setPrefColumnCount(4);
        simulare =new TextField();
        simulare.setPrefColumnCount(4);
        err.setEditable(false);
        err.setBackground(Background.EMPTY);
        err.setPrefColumnCount(50);
        err.setPrefRowCount(50);
        run.setOnAction(this);
    }

    private void render(GraphicsContext g){
        g.clearRect(0, 0, 510, 270);//pentru clear
        if(panou!=null) {
            for (int i = 0; i < panou.getList().size(); i++) {
                g.setFill(Color.BLUE);
                String s=String.valueOf(panou.getList().get(i).getWaitTime());//afiseaza wait time langa casa
                g.fillText(s,0,(i+1)*25+14,16);
                g.fillRect(25,(i+1)*25,10,20);
                for (int j = 0; j < panou.getList().get(i).getList().size(); j++) {
                    g.setFill(Color.RED);
                    g.fillOval((25+((j+1)*20)),(i+1)*25,20,20);
                    g.setFill(Color.BLACK);
                    String c=String.valueOf(panou.getList().get(i).getList().get(j).getId());//numarul clientului este afisat
                    g.fillText(c,(25+((j+1)*20)+5),(i+1)*25+13,10);
                }
            }
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        initValues();//initializam variabilele de clasa
        stage.setTitle("Simulare");
        stage.setResizable(false);
        Label ariv = new Label("Min Arrive Time :");
        Label ariv1 = new Label("Max Arrive Time :");
        Label simul = new Label("Simulation Time :");
        Label serv = new Label("Min Service Time :");
        Label serv2 = new Label("Max Service Time :");
        Label nCase = new Label("Numar de Cozi :");
        Label span = new Label("Speed :");
        Label strategy = new Label("  Strategy.Strategy :");
        GridPane sim= new GridPane();
        sim.setMinSize(420,130);
        sim.setVgap(5);
        sim.setHgap(5);
        GraphicsContext gc=canvas.getGraphicsContext2D();
        sim.add(nCase,0,0);
        sim.add(nrCase,1,0);
        sim.add(serv,0,1);
        sim.add(servMin,1,1);
        sim.add(serv2,2,1);
        sim.add(servMax,3,1);
        sim.add(ariv,0,2);
        sim.add(arivMin,1,2);
        sim.add(ariv1,2,2);
        sim.add(arivMax,3,2);
        sim.add(simul,0,3);
        sim.add(simulare,1,3);
        VBox v =new VBox();
        HBox h =new HBox();
        HBox mini =new HBox();
        mini.setSpacing(5);
        mini.getChildren().addAll(span,this.span,strategy,this.strategy);
        Pane p = new Pane();
        Label s=new Label("Simulare");
        p.getChildren().addAll(s,canvas);
        new AnimationTimer()//timer pentru randare
        {
            public void handle(long currentNanoTime)
            {
                render(gc);//functia pentru simulare grafica
            }
        }.start();
        v.setPadding(new Insets(10,10,10,10));
        h.setPadding(new Insets(10,10,10,10));
        h.setSpacing(5);
        v.setSpacing(5);
        v.getChildren().addAll(sim,mini,run,p);
        h.getChildren().addAll(v,err);
        Scene scene = new Scene(h,1050,520);
        stage.setScene(scene);
        stage.show();
        stage.setOnCloseRequest(windowEvent -> System.exit(0));//se inchide programul cu tot cu thread si timer
    }

    @Override
    public void handle(ActionEvent event) {//de aici pornesc simularea de la butonul RUN
        if(event.getSource()==run) {
            int cozi, sMin, sMax, aMin, aMax, sim;
            try {
                cozi = Integer.parseInt(nrCase.getText());
                sMin = Integer.parseInt(servMin.getText());
                sMax = Integer.parseInt(servMax.getText());
                aMin = Integer.parseInt(arivMin.getText());
                aMax = Integer.parseInt(arivMax.getText());
                sim = Integer.parseInt(simulare.getText());
                if(cozi<0 || cozi>10 || sMin>sMax|| aMin>aMax ||sim==0)throw new Exception();
            }catch (Exception e){
                err.setText("Nu ati introdus o valoare valida!");
                return;
            }
            SPEED= Integer.parseInt(String.valueOf(span.getValue().toString().charAt(0)));
            String s=strategy.getValue().toString();
            panou=null;
            panou= new Panou(cozi,sMin,sMax,aMin,aMax,sim,s);
            Panou.curentTime=0;
            panou.start();
            err.setText("");
        }
    }

    public static void main(String[] args){
        launch(args);
    }
}