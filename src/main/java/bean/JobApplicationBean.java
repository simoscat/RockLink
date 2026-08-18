package bean;

import java.math.BigDecimal;

public class JobApplicationBean extends ApplicationBean{

    private BigDecimal raiseOffer;

    public JobApplicationBean() {
    }

    public JobApplicationBean(BigDecimal raiseOffer) {
        this.raiseOffer = raiseOffer;
    }

    public BigDecimal getRaiseOffer() {
        return raiseOffer;
    }

    public void setRaiseOffer(BigDecimal raiseOffer) {
        this.raiseOffer = raiseOffer;
    }


}
