package sample;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import javax.imageio.ImageIO;
import java.io.File;

public class Controller {

    private boolean toolSelected = false; // кисть выключена
    private boolean pencilTool = true; // карандаш включен по умолчанию
    private boolean clear = false; // ластик выключен
    private boolean drawoval = false; // овал выключен
    private boolean drawline = false; // линия выключена
    private boolean drawrectangle = false;  // квадрат выключен

    private double size; // переменная для размера инструментов
    private double startX; // стартовая точка оси х
    private double startY; // стартовая точка оси y
    private double lastX; // конечная точка оси х
    private double lastY; // конечная точка оси y


    @FXML
    private GraphicsContext brushTool; // инструмент, с помощью которого рисуют

    @FXML
    private GraphicsContext effect; // инструмент, с помощью которого создают эффекты

    @FXML
    private ToggleButton zalivka; // залитая фигура или контур

    @FXML
    private ColorPicker colorpicker; // цветовая палитра

    @FXML
    private TextField bsize; // изменять размер фигуры

    @FXML
    private Canvas canvas; // холст

    @FXML
    private Canvas canvasGO; // холст для эффектов


    @FXML
    private void initialize() {

        brushTool = canvas.getGraphicsContext2D(); // даём возможность рисовать
        effect = canvasGO.getGraphicsContext2D();

        canvas.setOnMousePressed(event -> { // при нажатии мыши
                    // начало рисования
                    startX = event.getX(); // получаем точку, в которую кликнули на оси x
                    startY = event.getY(); // получаем точку, в которую кликнули на оси y

                    // рисование карандашом. Только черного цвета и размера по умолчанию
                    if (pencilTool) { //если выбран инструмент карандаш
                        brushTool.setFill(Color.BLACK); // устанавливаем черный цвет
                        brushTool.beginPath(); // чтобы линия была не непрерывной, а заканчивалась
                        brushTool.setLineWidth(1);// размер линии
                        brushTool.lineTo(startX, startY); // начинаем с этих координат
                        brushTool.stroke();
                    }
                }
        );

        canvas.setOnMouseDragged(event -> { // при перемещении нажатым курсором мыши

            size = Double.parseDouble(bsize.getText()); // получаем размер инструмента
            double x = event.getX() - size / 2; // получаем точку, в которой находится мышь, на оси x
            double y = event.getY() - size / 2; // получаем точку, в которой находится мышь, на оси y
            lastX = event.getX();
            lastY = event.getY();

            // рисуем кистью
            if (toolSelected) { // если выбрана кисть
                brushTool.setFill(colorpicker.getValue()); // присваиваем цвет
                brushTool.fillRoundRect(x, y, size, size, size, size); // рисуем кругами по координатам
            }

            // продолжение рисования карандашом
            if (pencilTool) { // если выбран карандаш
                brushTool.setFill(Color.BLACK); // цвет по умолчанию черный
                brushTool.lineTo(lastX, lastY); // рисуем по координатам
                brushTool.stroke();
            }

            // ластик
            if (clear) { // если выбран ластик
                brushTool.clearRect(x, y, size, size); // стираем квадратом установленного размера с холста для рисунков
                effect.clearRect(x, y, size, size); // стираем квадратом установленного размера с холста для эффектов
            }

            // эффекты, чтобы было видно как рисуем фигуры и линию

            if (drawrectangle) // если выбран квадрат
                drawRectEffect(); // вызывается метод эффекта квадрата
            if (drawoval) // если выбран овал
                drawOvalEffect(); // вызывается метод эффекта овала
            if (drawline) // если выбрана линия
                drawLineEffect(); // вызывается метод эффекта линии
        });

        canvas.setOnMouseReleased(event -> { // при отпускании мыши. Здесь вырисовываются фигуры
            if (drawrectangle) // если выбран квадрат
                drawRect(); // вызывается метод рисования квадрата
            if (drawoval) // если выбран овал
                drawOval(); // вызывается метод рисования овала
            if (drawline) // если выбрана линия
                drawLine(); // вызывается метод рисования линии
        });
    }

    //>>>>>>>>>>>>>>>>>>>>>>>>>>>методы кнопок<<<<<<<<<<<<<<<<<<<<<<<<<
    //>>>>>>>>>>>>>>>>>>>>>>>>здесь происходит включение кнопки<<<<<<<<<<<<<<<<<<<<<<<
    ///////////////////////////////////////////////////////////////////////////////////

    // линия
    @FXML
    private void Line(ActionEvent event) { // при нажатии на кнопку
        esc(); // сбросить все переменные на false
        drawline = true; // переменная с рисованием линии стала true, чтобы был включён только один инструмент
    }

    // овал
    @FXML
    private void Oval(ActionEvent event) { // при нажатии на кнопку
        esc(); // сбросить все переменные на false
        drawoval = true; // переменная с рисованием овала стала true, чтобы был включён только один инструмент
    }

    // квадрат
    @FXML
    private void Rect(ActionEvent event) { // при нажатии на кнопку
        esc(); // сбросить все переменные на false
        drawrectangle = true; // переменная с рисованием квадрата стала true, чтобы был включён только один инструмент
    }

    // кисть
    @FXML
    private void toolSelected(ActionEvent event) { // при нажатии на кнопку
        esc(); // сбросить все переменные на false
        toolSelected = true; // переменная с рисованием кистью стала true, чтобы был включён только один инструмент
    }

    // карандаш
    @FXML
    private void pencilTool(ActionEvent event) { // при нажатии на кнопку
        esc(); // сбросить все переменные на false
        pencilTool = true; // переменная с рисованием карандашом стала true, чтобы был включён только один инструмент
    }

    // ластик
    @FXML
    private void clear(ActionEvent event) { // при нажатии на кнопку
        esc(); // сбросить все переменные на false
        clear = true; // переменная с ластиком стала true, чтобы был включён только один инструмент
    }

    // этот метод сохраняет холст в папку по умолчанию
    @FXML
    private void onSave() {
        try {
            WritableImage snapshot = canvas.snapshot(null, null);
            ImageIO.write(SwingFXUtils.fromFXImage(snapshot, null), "png", new File("C:\\Users\\corob\\IdeaProjects\\Bobolink FX\\bobolink.png"));
        } catch (Exception e) {
            System.out.println("Failed to save image: " + e);
        }
    }

    // выход из программы
    @FXML
    private void onExit() {
        Platform.exit();
    }

    // кнопка очистить
    @FXML
    private void clearCanvas(ActionEvent event) { // при нажатии на кнопку
        brushTool.clearRect(0, 0, canvas.getWidth(), canvas.getHeight()); // очищается холст для рисования
        effect.clearRect(0, 0, canvas.getWidth(), canvas.getHeight()); // очищается холст с эффектами
    }

    // снять все инструменты
    @FXML
    private void esc(ActionEvent event) { // при нажатии на кнопку
        esc(); // сбросить все переменные на false
    }

    // метод снимает все инструменты
    private void esc() {
        toolSelected = false; // кисть отключена
        pencilTool = false; // карандаш
        clear = false; // ластик
        drawoval = false; // овал
        drawline = false; // линия
        drawrectangle = false; // квадрат
    }


    //>>>>>>>>>>>>>>>>>>>>>>>>>>>методы рисования<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


    private void drawRect() { // рисование прямоугольников
        double wh = lastX - startX; // переменная для того, чтобы узнать край ширины
        double hg = lastY - startY; // переменная для того, чтобы узнать край высоты
        brushTool.setLineWidth(size); // получаем размер контура и присваем его инструменту, с помощью которого производится рисование

        if (zalivka.isSelected()) { // если кнопка "Заливка фигуры" нажата, то...
            brushTool.setFill(colorpicker.getValue()); // получаем выбранный цвет и наполняем им инструмент
            brushTool.fillRect(startX, startY, wh, hg); // получаем значения местонахождения курсора мыши в начальной точке и краям и закрашиваем прямоугольник
        } else { // иначе
            brushTool.setStroke(colorpicker.getValue()); // получаем выбранный цвет и наполняем им контур
            brushTool.strokeRect(startX, startY, wh, hg); // получаем значения местонахождения курсора мыши в начальной точке и краям и закрашиваем контур прямоугольника
        }
    }

    private void drawLine() { // рисование линий
        brushTool.setLineWidth(size); // получаем размер контура и присваем его инструменту, с помощью которого производится рисование
        brushTool.setStroke(colorpicker.getValue()); // присваиваем цвет
        brushTool.strokeLine(startX, startY, lastX, lastY); // риусем линию
    }

    private void drawOval() { // рисование овалов
        double wh = lastX - startX; // переменная для того, чтобы узнать край ширины
        double hg = lastY - startY; // переменная для того, чтобы узнать край высоты
        brushTool.setLineWidth(size); // получаем размер контура и присваем его инструменту, с помощью которого производится рисование

        if (zalivka.isSelected()) { // если кнопка "Заливка фигуры" нажата, то...
            brushTool.setFill(colorpicker.getValue()); // получаем выбранный цвет и наполняем им инструмент
            brushTool.fillOval(startX, startY, wh, hg); // получаем значения местонахождения курсора мыши в начальной точке и краям и закрашиваем овал
        } else { // иначе
            brushTool.setStroke(colorpicker.getValue()); // получаем выбранный цвет и наполняем им контур
            brushTool.strokeOval(startX, startY, wh, hg); // получаем значения местонахождения курсора мыши в начальной точке и краям и закрашиваем контур овала
        }
    }

    //>>>>>>>>>>>>>>>>>>>>>>>>>>Эффекты при рисовании<<<<<<<<<<<<<<<<<<<<<<<

    private void drawRectEffect() { // эффект при рисовании квадрата
        double wh = lastX - startX; // переменная для того, чтобы узнать край ширины
        double hg = lastY - startY; // переменная для того, чтобы узнать край высоты
        effect.setLineWidth(size); // присваиваем размер контура инструмента

        if (zalivka.isSelected()) { // если кнопка "Заливка фигуры" нажата, то...
            effect.clearRect(0, 0, canvas.getWidth(), canvas.getHeight()); // сразу же стерает проведенные квадраты
            effect.setFill(colorpicker.getValue()); // получаем выбранный цвет и наполняем им инструмент
            effect.fillRect(startX, startY, wh, hg); // получаем значения местонахождения курсора мыши в начальной точке и краям и закрашиваем квадрат
        } else { // иначе
            effect.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            effect.setStroke(colorpicker.getValue()); // получаем выбранный цвет и наполняем им контур инструмента
            effect.strokeRect(startX, startY, wh, hg); // получаем значения местонахождения курсора мыши в начальной точке и краям и закрашиваем контур квадрата
        }
    }

    private void drawLineEffect() { // эффект при рисовании линии
        effect.setLineWidth(size); // инструмент получает размер контура
        effect.setStroke(colorpicker.getValue()); // инструмент получает цвет
        effect.clearRect(0, 0, canvas.getWidth(), canvas.getHeight()); // сразу же стерает то место, где провели линию
        effect.strokeLine(startX, startY, lastX, lastY); // рисуется только последнее местоположение линии
    }

    private void drawOvalEffect() { // эффект при рисовании овала
        double wh = lastX - startX; // переменная для того, чтобы узнать край ширины
        double hg = lastY - startY; // переменная для того, чтобы узнать край высоты
        effect.setLineWidth(size); // присваиваем размер контура инструмента

        if (zalivka.isSelected()) { // если кнопка "Заливка фигуры" нажата, то...
            effect.clearRect(0, 0, canvas.getWidth(), canvas.getHeight()); // сразу же стерает то место, где провели овалом
            effect.setFill(colorpicker.getValue()); // получаем выбранный цвет и наполняем им инструмент
            effect.fillOval(startX, startY, wh, hg); // получаем значения местонахождения курсора мыши в начальной точке и краям и закрашиваем овал
        } else { // иначе
            effect.clearRect(0, 0, canvas.getWidth(), canvas.getHeight()); // сразу же стерает то место, где провели овалом
            effect.setStroke(colorpicker.getValue()); // присваиваем размер контура инструмента
            effect.strokeOval(startX, startY, wh, hg); // получаем значения местонахождения курсора мыши в начальной точке и краям и закрашиваем контур овала
        }
    }
}