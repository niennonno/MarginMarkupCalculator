package mapplinks.example.adityavikram.calculator;

import com.mapplinks.calculator.Taxes;

import java.util.ArrayList;

/**
 * Created by Aditya Vikram on 8/26/2015.
 */

public class TaxList extends ArrayList<Taxes> {
    private static TaxList sInstance = null;

    private TaxList(){}

    public static TaxList getsInstance(){
        if(sInstance == null){
            sInstance = new TaxList();
        }
        return sInstance;
    }
}
