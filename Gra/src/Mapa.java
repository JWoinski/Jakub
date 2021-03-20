import java.awt.*;

public class Mapa {
    public int map[][];
    public int brickWidth;
    public int brickHeight;
    public Mapa(int row, int column){
        //tworzymy tutaj wymiary klocków do zbicia
        map = new int[row][column];
        for( int i =0; i<map.length;i++){
            for(int j=0; j< map[0].length; j++){
                map[i][j]=1;
            }
        }
        brickWidth=540/column;
        brickHeight= 150/row;
    }
    public void draw(Graphics2D graphics2D){
        //W części draw klocki forem w zalezności od tego, który jest to klocek są rysowane w zaleznosci od wielkosci jakie wybraliśmy powyzej w zmiennej brickwidth i brickheight
        for( int i =0; i<map.length;i++){
            for(int j=0; j< map[0].length; j++) {
                if(map[i][j]>0){
                    graphics2D.setColor(Color.cyan);
                    graphics2D.fillRect(j*brickWidth+80, i*brickHeight+50, brickWidth, brickHeight);
                    graphics2D.setStroke(new BasicStroke(3));
                    graphics2D.setColor(Color.black);
                    graphics2D.drawRect(j*brickWidth+80, i*brickHeight+50, brickWidth, brickHeight);

                }
            }
        }
    }
    public void setBrickValue(int value, int row,int column){
        //funkcja odpowiedzialna w dalszej części kodu za brak bądź widoczność bloku do zbicia
        map[row][column]=value;
    }
}
