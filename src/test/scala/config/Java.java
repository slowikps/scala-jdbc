package config;

import java.time.Duration;
import java.time.Instant;

public class Java {
    public static void main(String[] in) {
        Instant start = Instant.now();

        int x = 0;
        for(int i = 0; i < 90000; i++){
            if(x % 4 == 1) {
                x += i;
            }else {
                x -= i;
            }
        }

        System.out.println(Duration.between(start, Instant.now()).toMillis());
        System.out.println(x);
    }
}
