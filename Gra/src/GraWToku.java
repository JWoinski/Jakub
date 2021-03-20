import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class GraWToku extends JPanel implements KeyListener, ActionListener {
    Random random = new Random();
    private boolean gameStillAlive=false; //na poczatek ustawiamy, ze gra jest nieaktywna
    private int points=0; //punkty na start
    private int everyBricks=21; //wszystkie klocki
    private Timer time;
    private int delay = 8;
    private int playerX= 310; //Koordynaty pojawienia się odbijaczki
    private int positionOfBallX=random.nextInt(700); // Mozna przypisać stałą wartość, ale ja zastosowałem mozliwość wyloswania w kazdym mozliwym miejscu oprócz miejsca w którym są klocki
    private int positionOfBallY=random.nextInt(400)+300;//zmienna opisana wyzej, ale ta zmienna jest dla osi Y
    private int DirectionOfBallX=-1; //predkosc poruszania się piłeczki po osi X
    private int DirectionOfBallY=-2; //predkosc poruszania się piłeczki po osi Y
    private int lives=3;
    private Mapa map; //obiekt mapy(klocków)
    private BufferedImage temp; //obiekt wyłowyanego zdjęcia
    public GraWToku() throws IOException {
        //w konstruktorze ustawiamy wszystkie wartosci na takie by stworzyć odpowiedni obiekt mapy i dac mozliwosci poruszania się graczowi. W momencie ruszenia się odbijaczki w grze zaczyna lecieć czas
        map= new Mapa(3,7);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        time = new Timer(delay,this);
        time.start();
        temp= ImageIO.read(new File("Background.png"));


    }
    @Override
    public void paint(Graphics graphics){
        //bg
        graphics.drawImage(temp,0,0,700,600,new ImagePanel(temp));
        // mapa
        map.draw((Graphics2D) graphics);
        // rogi
        graphics.setColor(Color.yellow);
        graphics.fillRect(0,0,3,600);
        graphics.fillRect(0,0,700,3);
        graphics.fillRect(700 ,0,3,600);
        //wynik
        graphics.setColor(Color.white);
        graphics.setFont(new Font("Arial",Font.BOLD,25));
        graphics.drawString("Punkty: "+points,570,30);
        //zycia
        graphics.setColor(Color.white);
        graphics.setFont(new Font("Arial",Font.BOLD,25));
        graphics.drawString("Zycia: "+lives,10,30);
        // odbijaczka
        graphics.setColor(Color.yellow);
        graphics.fillRect(playerX,550,100,5);
        //piłka
        graphics.setColor(Color.GREEN);
        graphics.fillOval(positionOfBallX,positionOfBallY,20,20);

        if(everyBricks<=0){
            //warunek odpowiedzialny za sprawdzanie czy gra już się zakończyła ze względu na brak klocków do zbicia
            graphics.setColor(Color.white);
            graphics.drawString("Wygrałeś",250,300);

            graphics.setColor(Color.white);
            graphics.drawString("Kliknij Enter żeby zagrać jeszcze raz",120,350);

        }
        if(positionOfBallY>700 && lives==0){
            //warunek odpowiedzialny za sprawdzenie czy piłka wypadła z planszy i czy życia spadły już poniżej zera by zakończyć grę
            graphics.setColor(Color.white);
            graphics.drawString("Przegrałeś, twoje punkty to: "+points,150,300);
            graphics.setColor(Color.white);
            graphics.drawString("Kliknij Enter żeby zagrać jeszcze raz",120,350);
        }


        graphics.dispose();
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        time.start();
        if(gameStillAlive){
            if(new Rectangle(positionOfBallX,positionOfBallY,20,20).intersects(new Rectangle(playerX,550,100,8)))
            //warunek odpowiedzialny za sprawdzenie czy pileczka dotknela odbijaczki jesli tak to ustawia jej kierunek na przeciwny
            {
                DirectionOfBallY=-DirectionOfBallY;
            }
            temp: for(int i=0;i<map.map.length;i++){
                // pierwsza map to klasa Mapa a druga to tablica
                 for(int j= 0; j<map.map[0].length; j++){
                    if(map.map[i][j]>0){
                        //ustaweiam po kolei wielkosci bloczkow na mapie
                        int brickX = j*map.brickWidth +80;
                        int brickY =i * map.brickHeight +50;
                        int brickWidth = map.brickWidth;
                        int brickHeight = map.brickHeight;

                        Rectangle rectangle = new Rectangle(brickX,brickY,brickWidth,brickHeight);
                        Rectangle ballRectangle = new Rectangle(positionOfBallX,positionOfBallY,20,20);
                        Rectangle brickRectangle = rectangle;

                        if(ballRectangle.intersects(brickRectangle))
                        //Warunek sprawdzający czy pilka uderzyla bloczek jeśli tak to ustawia
                            // jego value na 0 i usuwa go z planszy zmniejsza ilosc bloczkow do zbicia i dodaje nam 1 punkt za to
                        {
                            map.setBrickValue(0,i,j);
                            everyBricks--;
                            points++;

                            if(positionOfBallX +19 <=brickRectangle.x || positionOfBallX+1 >= brickRectangle.x + brickRectangle.width)
                            //warunek sprawdzający  w jaki sposob pileczka uderzyla bloczek i zmienia odpowiednio jej kierunek po X lub Y
                            {
                                DirectionOfBallX=-DirectionOfBallX;
                            }
                            else{
                                DirectionOfBallY=-DirectionOfBallY;
                            }
                            break temp;
                        }
                    }
                }
            }
            //Zmieniamy pozycje za pomocą direction dlatego im większą liczbę damy przy direction tym szybciej bedzie kulka się poruszać
            positionOfBallX +=DirectionOfBallX;
            positionOfBallY +=DirectionOfBallY;
            if(positionOfBallX >= 692 || positionOfBallX <=0)
            //Warunek odpowiedzialny za sprawdzanie czy kulka uderzyła w sciana i odpowiednio zmienia jej kierunek po X lub po Y
                {
                DirectionOfBallX= - DirectionOfBallX;
            }
            if(positionOfBallY  < 0){
                DirectionOfBallY = - DirectionOfBallY;
            }
        }
        if(everyBricks<=0)
        //jesli wszystkie klocki spadną do 0 to gra sie zatrzymuje
        {
            gameStillAlive = false;
            DirectionOfBallY = 0;
            DirectionOfBallX = 0;
        }
        else if(positionOfBallY>700 && lives<0)
        //jeśli kulka spadla ponizej odbijaczki i juz zycia są ponizej zera to gra sie zatrzymuje
        {
            gameStillAlive = false;
            DirectionOfBallY = 0;
            DirectionOfBallX = 0;
        }
        else if(positionOfBallY>700)
        //Jesli kulka spadla ponizej odbijaczki ale nadal mamy zycia to kulka pojawia sie na
            // nowo w randomowym miejscu i spadam nam zycie o jeden + randomowo wybierami kierunek X w ktorym poleci
        {
            positionOfBallX = random.nextInt(700);
            positionOfBallY = random.nextInt(550) + 300;
            int temp = random.nextInt(100 + 100) - 100;
            if (temp > 0) {
                temp = 1;
            } else {
                temp = -1;
            }
            DirectionOfBallX = temp;
            DirectionOfBallY = -2;
            lives -= 1;

        }

        repaint();

    }

    @Override
    public void keyTyped(KeyEvent e) { }
    @Override
    public void keyReleased(KeyEvent e) { }
    @Override
    public void keyPressed(KeyEvent button) {
        if(button.getKeyCode()==KeyEvent.VK_RIGHT) {
            if(playerX>=600)
            //jesli koordynaty odbijaczki są za duze by isc w prawo to nic sie nie dzieje a w przeciwnym przypadku są przepsuwane
            {
                playerX=600;
            }
            else{
                moveRight();
            }
        }
        if(button.getKeyCode()==KeyEvent.VK_LEFT)
        //To samo co dla prawej tylko, że na lewo
        {
            if(playerX < 10){
                playerX = 10;
            }
            else{
                moveLeft();
            }
        }
        if(button.getKeyCode()==KeyEvent.VK_ENTER){
            //Po kliknięciu enter gra jest rozpoczynana i ustawa gamestillalive na aktywną randomem ustawia piłęczkę i jej kierunek i wszelkie inne potrzebne rzeczy do wznowienia gry
            if(!gameStillAlive){
                gameStillAlive=true;
                positionOfBallX=random.nextInt(700);
                positionOfBallY=random.nextInt(550)+300;
                int temp=random.nextInt(100+100)-100;
                if(temp>0){
                    temp=1;
                }
                else{
                    temp=-1;
                }
                DirectionOfBallX=temp;
                DirectionOfBallY=-2;
                everyBricks=21;
                points=0;
                lives=3;
                map = new Mapa(3,7);
                repaint();
            }
        }
    }
    public void moveRight()
    //przesuwa odbijaczke w prawo o 15 pikseli
    {
        gameStillAlive = true;
        playerX+=15;
    }
    public void moveLeft()
    //Przesuwa odbijaczke w lewo o 15 pikseli
    {
        gameStillAlive = true;
        playerX-=15;
    }

    //key listener do wykrywania przycisków "strzałek"
    //actionlistener do ruchu kulki


}
