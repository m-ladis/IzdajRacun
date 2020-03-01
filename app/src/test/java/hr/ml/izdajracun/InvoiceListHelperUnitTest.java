package hr.ml.izdajracun;

import org.junit.Assert;
import org.junit.Test;

import hr.ml.izdajracun.utils.InputFieldValidator;

public class InvoiceListHelperUnitTest {

    @Test
    public void isHrIban_ValidHrIbans_ExpectReturnTrue(){
        Assert.assertTrue(InputFieldValidator.isHrIban("HR2124070003205695542"));
        Assert.assertTrue(InputFieldValidator.isHrIban("HR4124070003104577920"));
        Assert.assertTrue(InputFieldValidator.isHrIban("HR2224070003104968008"));
        Assert.assertTrue(InputFieldValidator.isHrIban("HR5324070003104943491"));
    }

    @Test
    public void isHrIban_InvalidHrIbans_ExpectReturnFalse(){
        Assert.assertFalse(InputFieldValidator.isHrIban("HR2124070003205695541"));
        Assert.assertFalse(InputFieldValidator.isHrIban("HRHR24070003205695541"));
    }

    @Test
    public void isPriceValid_ValidTotalPrices_ExpectReturnTrue(){
        Assert.assertTrue(InputFieldValidator.isPriceValid(5, 500, 2500));
        Assert.assertTrue(InputFieldValidator.isPriceValid(5, 500, 2400));
    }

    @Test
    public void isPriceValid_InvalidTotalPrices_ExpectReturnFalse(){
        Assert.assertFalse(InputFieldValidator.isPriceValid(5, 500, 2600));
        Assert.assertFalse(InputFieldValidator.isPriceValid(5, 500, 2700));
    }
}
