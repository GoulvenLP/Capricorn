package enstabretagne.moniteur2D;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import enstabretagne.base.logger.Logger;
import enstabretagne.base.time.LogicalDateTime;
import enstabretagne.base.time.LogicalDuration;
import enstabretagne.engine.InitData;
import enstabretagne.engine.SimuEngine;
import enstabretagne.engine.SimuScenario;
import enstabretagne.engine.SimuScenarioInitData;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class MoniteurGraphique2D extends Application {

	// Noms à donner aux paramètres pour initialiser le moniteur JavaFX (capture des
	// args)

	private SimuEngine engine;
	MonitorState state;
	// Service qui fait la transformation des objets simulés en objets graphiques
	VisualConverter visualConverter;
	List<Shape> visualDataModel;

	// Boucle de simulation monothreadée
	Timeline boucleSimulation;
	int simuFrameDuration = 20;// ms

	// Boucle de rafraichissement du compteur monothreadée
	Timeline boucleAffichageTempsReelEcoule;
	private int frameCount;

	private Jsonb jsonb;
	SimuScenarioInitData scenario;
	Constructor<SimuScenario> construct;


	private Button button1;
	private Button button2;
	private Button button3;
	private Button button4;

	private Label label1;
	private Label label2;

	private ToolBar toolbar;

	private BorderPane root;

	private Canvas objectCanvas;
	private Canvas backGroundCanvas;

	private Pane canvasContainer;
	private ScrollPane scrollPane;

	private double canvasMinWidth;
	private double canvasMinHeight;

	public class RequiredParams {
		public static final String ScenarioType = "ScenarioType";
		public static final String ScenarioInitType = "ScenarioInitType";
		public static final String ScenarioInit = "ScenarioInit";
		public static final String VisualConverter = "VisualConverter";
	}

	// Etat du moniteur graphique
	public enum MonitorState {
		STOP, RUN, INITIALIZED, PAUSE
	};

	@Override
	public void start(Stage primaryStage) {

		state = MonitorState.STOP;
		loadGUIConfiguration();
		initGUI(primaryStage);

		boucleAffichageTempsReelEcoule = new Timeline(new KeyFrame(Duration.millis(1000), event -> {
			frameCount++;
			setLabels(); // Met à jour le texte
		}));
		boucleAffichageTempsReelEcoule.setCycleCount(Timeline.INDEFINITE); // Répéter indéfiniment

		boucleSimulation = new Timeline(new KeyFrame(Duration.millis(simuFrameDuration), event -> {
			engine.simulate(LogicalDuration.ofMillis(simuFrameDuration));
			visualConverter.updateVisualDataModel();
		}));
		boucleSimulation.setCycleCount(Timeline.INDEFINITE); // Répéter indéfiniment

		primaryStage.setOnCloseRequest(e -> {
			System.out.println("[*] Fermeture de l'application");
			stopSimulation();
			primaryStage.close();
			Logger.Terminate();
		});

		primaryStage.show();

	}


	protected void loadGUIConfiguration() {
		JsonbConfig config = new JsonbConfig().withFormatting(true);
		jsonb = JsonbBuilder.create(config);

		var params = getParameters();
		var scType = params.getNamed().get(RequiredParams.ScenarioType);
		var scIniType = params.getNamed().get(RequiredParams.ScenarioInitType);
		var scIni = params.getNamed().get(RequiredParams.ScenarioInit);
		var convs = params.getNamed().get(RequiredParams.VisualConverter);

		try {

			Class c = getClass().forName(convs);
			visualConverter = (VisualConverter) c.getConstructor().newInstance();

			Class simuScClass = getClass().forName(scType);

			Class simuSciniClass = getClass().forName(scIniType);
			scenario = (SimuScenarioInitData) jsonb.fromJson(scIni, simuSciniClass);

			construct = simuScClass.getConstructor(SimuEngine.class,InitData.class);

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}

		canvasMinWidth = 800;
		canvasMinHeight = 600;

		visualDataModel = new ArrayList<Shape>();

	}



	protected void initGUI(Stage primaryStage) {
		button1 = new Button("Init Simulation");
		button1.setPrefWidth(150);
		button2 = new Button("Play");
		button2.setPrefWidth(150);
		button3 = new Button("Stop");
		button3.setPrefWidth(150);
		button4 = new Button("AFAP");
		button4.setPrefWidth(150);
		label1 = new Label("");
		label2 = new Label("");

		toolbar = new ToolBar();
		toolbar.getItems().addAll(button1, button2,button4, button3, label1, label2);

		setLabels();

		objectCanvas = new Canvas(canvasMinWidth, canvasMinHeight);
		backGroundCanvas = new Canvas(canvasMinWidth, canvasMinHeight);


		// Gestion des actions des boutons
		button1.setOnAction(e -> initScenario());
		button2.setOnAction(e -> playOrPauseSimulation());
		button3.setOnAction(e -> stopSimulation());
		button4.setOnAction(e-> playAsFastAsPossible());

		canvasContainer = new Pane();
		scrollPane = new ScrollPane(canvasContainer);
		scrollPane.setPannable(true);

		// Gestion du zoom avec la molette de la souris
		canvasContainer.setOnScroll(event -> {
			if (event.getDeltaY() != 0) {
				double scaleFactor = (event.getDeltaY() > 0) ? 1.1 : 0.9;
				canvasContainer.setScaleX(canvasContainer.getScaleX() * scaleFactor);
				canvasContainer.setScaleY(canvasContainer.getScaleY() * scaleFactor);
				event.consume();
			}
		});
		canvasContainer.getChildren().addAll(backGroundCanvas, objectCanvas);

		// Disposition principale (BorderPane)
		root = new BorderPane();
		root.setTop(toolbar); // Place la barre d'outils en haut
		root.setCenter(scrollPane); // Place la zone de dessin au centre

		// Création de la scène
		Scene scene = new Scene(root);

		primaryStage.setTitle("Barre d'outils et zone de dessin");
		primaryStage.setScene(scene);

		primaryStage.setMaximized(true); // Maximiser la fenêtre
		primaryStage.initStyle(StageStyle.DECORATED); // Assurer que le bouton de fermeture est présent

	}



	protected void initScenario() {

		if (state == MonitorState.STOP) {
			engine = new SimuEngine();

			SimuScenario s = null;
			try {
				s = construct.newInstance(engine, scenario);
				engine.initSimulation(s,new LogicalDateTime(scenario.start), new LogicalDateTime(scenario.end));
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			visualConverter.init(engine, backGroundCanvas, objectCanvas, canvasMinWidth, canvasMinHeight);
			visualConverter.updateVisualDataModel();

			
			state = MonitorState.INITIALIZED;

		}

	}

	public void setLabels() {
		label1.setText("Temps réel : " + frameCount + " s");
		if (engine != null)
			label2.setText("Temps logique : "+ engine.Now());
		else
			label2.setText("Temps logique : ");

	}

	protected void playOrPauseSimulation() {
		if (state == MonitorState.PAUSE || state == MonitorState.INITIALIZED) {
			state = MonitorState.RUN;
			boucleAffichageTempsReelEcoule.play();
			boucleSimulation.play();
			button2.setText("Pause");

		} else if (state == MonitorState.RUN) {
			state = MonitorState.PAUSE;
			boucleAffichageTempsReelEcoule.pause();
			boucleSimulation.pause();
			button2.setText("Play");
		}

	}

	protected void playAsFastAsPossible() {
    	if(state == MonitorState.PAUSE || state == MonitorState.INITIALIZED)
    	{
    		engine.releaseFlag();
    		engine.simulate();
        	visualConverter.updateVisualDataModel();
        	setLabels();
    	}
    }

	protected void stopSimulation() {
		if (state == MonitorState.RUN || state == MonitorState.PAUSE ||state == MonitorState.INITIALIZED) {
			{
				state = MonitorState.STOP;
				boucleSimulation.stop();

				// visualConverter.updateVisualDataModel();
				boucleAffichageTempsReelEcoule.stop();

				button2.setText("Play");
				frameCount = 0;
				setLabels();
			}
		}
	}

}