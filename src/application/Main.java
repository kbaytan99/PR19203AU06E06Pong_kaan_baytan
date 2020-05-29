package application;

import javafx.application.Application;
import javafx.geometry.Bounds;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

public class Main extends Application {

	class Cuadrado {
		class Posicio {
			private double posx;
			private double posy;

			public Posicio(double x, double y) {
				this.posx = x;
				this.posy = y;
			}
		}

		class Medidas {
			private double width;
			private double height;

			public Medidas(double w, double h) {
				this.width = w;
				this.height = h;
			}
		}

		Posicio posicio;
		Medidas medidas;
		int velocitat = 10;
		Pane panell;
		Node cuadrado;
		Cuadrado cuadrado1;

		public Cuadrado(Pane panell, double posx, double posy, double width, double height) {
			posicio = new Posicio(posx, posy);
			medidas = new Medidas(width, height);
			this.panell = panell;
			this.cuadrado = new Rectangle(posicio.posx, posicio.posy, medidas.width, medidas.height);
			posicio.posx = 0;
			posicio.posy = 0;
			medidas.height = 10;
			medidas.width = 10;
			this.cuadrado.setLayoutX(posicio.posx);
			this.cuadrado.setLayoutY(posicio.posy);
			this.panell.getChildren().add(this.cuadrado);
		}

		private void pintar() {
			this.cuadrado.setLayoutX(posicio.posx);
			this.cuadrado.setLayoutY(posicio.posy);
		}

		public void ariba() {
			posicio.posy = posicio.posy - this.velocitat;
			this.pintar();
		}

		public void abajo() {
			posicio.posy = posicio.posy + this.velocitat;
			this.pintar();
		}

	}

	class Bola {
		public double deltaX;
		public double deltaY;
		Circle circulo;

		public Bola(int radio, Color color) {
			this.circulo = new Circle(radio, color);
			this.deltaX = 3;
			this.deltaY = 3;

		}

	}

	int puntuacion = 0;
	int puntuacionVel = 0;

	// VELOCIDAD BOLA CONFIGURABLE
	int velocidad_bola = 1;
	int radio_bola = 15;
	Bola bola1;
	Pane panell = new Pane();
	// MEDIDA DE LA PANTALL
	double w = 800;
	double h = 600;

	public void movimientoBola(Bola bola) {
		bola.circulo.setLayoutX(bola.circulo.getLayoutX() + bola.deltaX - (velocidad_bola));
		bola.circulo.setLayoutY(bola.circulo.getLayoutY() + bola.deltaY - (velocidad_bola));

		final Bounds limite = panell.getBoundsInLocal();
		final boolean limiteDerecha = bola.circulo
				.getLayoutX() >= (limite.getMaxX() - bola.circulo.getRadius() - bola.deltaX);
		final boolean limiteIzquierda = bola.circulo
				.getLayoutX() <= (limite.getMinX() + bola.circulo.getRadius() - bola.deltaX);
		final boolean limiteInferior = bola.circulo
				.getLayoutY() >= (limite.getMaxY() - bola.circulo.getRadius() - bola.deltaY);
		final boolean limiteSuperior = bola.circulo
				.getLayoutY() <= (limite.getMinY() + bola.circulo.getRadius() - bola.deltaY);

		if (puntuacionVel == 10) {
			velocidad_bola--;
			puntuacionVel = 0;
		}
		if (limiteDerecha) {
			trayectoriaX(bola);
			puntuacion++;
			puntuacionVel++;
		}
		if (limiteIzquierda) {
			trayectoriaX(bola);
			puntuacion = puntuacion - 2;
			puntuacionVel = puntuacionVel - 2;
		}
		if (limiteInferior || limiteSuperior) {
			trayectoriaY(bola);
		}

	}

	public boolean comprovaImpacte(Bola bola1, Cuadrado cuadrado1) {
		if (bola1.circulo.getBoundsInParent().intersects(cuadrado1.cuadrado.getBoundsInParent())) {
			trayectoriaX(bola1);
			trayectoriaY(bola1);
			return true;
		}
		return false;
	}

	public void trayectoriaX(Bola bola) {
		bola.deltaX = Math.signum(bola.deltaX) * (int) (Math.random() * 10 + 1);
		bola.deltaX *= -1;
	}

	public void trayectoriaY(Bola bola) {
		bola.deltaY = Math.signum(bola.deltaY) * (int) (Math.random() * 10 + 1);
		bola.deltaY *= -1;
	}

	public void comprovaBola(Bola bola1, Cuadrado cuadrado1) {
		comprovaImpacte(bola1, cuadrado1);
		movimientoBola(bola1);
	}

	@SuppressWarnings("incomplete-switch")
	public void start(Stage primaryStage) throws Exception {
		if (puntuacion < 0) {
			primaryStage.close();
			// El juego se termina si la puntuacion es -1
		}
		Label label = new Label("Puntuacion:");
		label.setTextFill(Color.BLACK);
		primaryStage.setScene(new Scene(panell, w, h));
		primaryStage.setTitle("PONG EL JUEGO");
		primaryStage.show();
		panell.requestFocus();
		bola1 = new Bola(radio_bola, Color.BLUE);
		Cuadrado cuadrado1 = new Cuadrado(panell, 0, 200, 20, 200);
		panell.getChildren().add(bola1.circulo);
		panell.setOnKeyPressed(e -> {

			switch (e.getCode()) {
			case W:
				cuadrado1.ariba();
				break;

			case S:
				cuadrado1.abajo();
				break;

			case UP:
				cuadrado1.ariba();
				break;

			case DOWN:
				cuadrado1.abajo();
				break;
			}

		});

		final Timeline loop = new Timeline(new KeyFrame(Duration.millis(10), new EventHandler<ActionEvent>() {

			@Override
			public void handle(final ActionEvent event) {
				comprovaBola(bola1, cuadrado1);
			}
		}));
		loop.setCycleCount(Timeline.INDEFINITE);
		loop.play();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
