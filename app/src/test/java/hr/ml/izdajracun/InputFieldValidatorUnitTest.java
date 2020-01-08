package hr.ml.izdajracun;

import org.junit.Assert;
import org.junit.Test;

import hr.ml.izdajracun.utils.InputFieldValidator;

public class InputFieldValidatorUnitTest {
    @Test
    public void isOib_ParameterWithInvalidLength_ExpectReturnFalse() {
        Assert.assertFalse(InputFieldValidator.isOib("83737841"));
    }

    @Test
    public void isOib_ParameterContainsNonNumberCharacters_ExpectReturnFalse() {
        Assert.assertFalse(InputFieldValidator.isOib("83737841sss"));
    }

    @Test
    public void isOib_ValidOIBAsParameter_ExpectReturnTrue() {
        Assert.assertTrue(InputFieldValidator.isOib("83730737841"));
    }

    @Test
    public void isOib_OIBWithNonValidControlNumberAsParameter_ExpectReturnFalse(){
        Assert.assertFalse(InputFieldValidator.isOib("11111111111"));
    }

    @Test
    public void isAnyStringEmpty_EmptyStringAsOneOfParameters_ExpectReturnTrue(){
        Assert.assertTrue(InputFieldValidator.isAnyStringEmpty("aaa", "bbb", ""));
    }

    @Test
    public void isAnyStringEmpty_AllParametersAreNonEmpty_ExpectReturnFalse(){
        Assert.assertFalse(InputFieldValidator.isAnyStringEmpty("aaa", "bbb"));
    }
}
