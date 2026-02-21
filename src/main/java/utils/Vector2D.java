package utils;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Vector2D{
    private double x, y;

    public void add(Vector2D other){
        this.x += other.x;
        this.y += other.y;
    }

    public void multiply(double scalar){
        this.x *= scalar;
        this.y *= scalar;
    }

    public double magnitude(){
        return Math.sqrt(x * x + y * y);
    }

    public void reset(){
        this.x = 0;
        this.y = 0;
    }
}