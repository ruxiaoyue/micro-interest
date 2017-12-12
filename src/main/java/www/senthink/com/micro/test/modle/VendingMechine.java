package www.senthink.com.micro.test.modle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by hyacinth on 2017/11/24.
 */
public class VendingMechine {

    private final static Logger LOGGER = LoggerFactory.getLogger(VendingMechine.class);


    /**
     * 已投币
     */
    private final static int INSERT_COIN = 0;
    /**
     * 未投币
     */
    private final static int NO_INSERT = 1;

    private final static int SOLD = 2;

    private final static int SOLD_OUT = 3;

    private int currentState = NO_INSERT;

    private int count = 0;

    public VendingMechine(int count) {
        this.count = count;
        if (count > 0) {
            this.currentState = NO_INSERT;
        }
    }

    /**
     * 投币
     */
    public void insertCoin() {
        switch (currentState) {
            case NO_INSERT:
                currentState = INSERT_COIN;
                LOGGER.debug("投币成功");
                break;
            case INSERT_COIN:
                LOGGER.debug("已投币 无需重复投币");
                break;
            case SOLD:
                LOGGER.debug("请稍等。。。。。");
                break;
            case SOLD_OUT:
                LOGGER.debug("已售罄 请勿投币");
                break;
        }
    }

    /**
     * 退币
     */
    public void returnCoin() {
        switch (currentState) {
            case NO_INSERT:
                LOGGER.debug("未投币");
                break;
            case INSERT_COIN:
                currentState = NO_INSERT;
                LOGGER.debug("退币成功");
                break;
            case SOLD:
                LOGGER.debug("商品已卖出 不予退币");
                break;
            case SOLD_OUT:
                LOGGER.debug("您未投币！！");
                break;
        }
    }

    /**
     * 转动手柄
     */
    public void trunCrank() {
        switch (currentState) {
            case NO_INSERT:
                LOGGER.debug("请先投币！！");
                break;
            case INSERT_COIN:
                currentState = SOLD;
                LOGGER.debug("正在出商品");
                //出货
                shipment();
                break;
            case SOLD:
                LOGGER.debug("连续转动也没用。。。");
                break;
            case SOLD_OUT:
                LOGGER.debug("商品已经售罄");
                break;
        }
    }

    /**
     * 出货
     */
    public void shipment() {
        switch (currentState) {
            case NO_INSERT:
            case INSERT_COIN:
            case SOLD_OUT:
                LOGGER.warn("非法状态");
                break;
            case SOLD:
                count--;
                LOGGER.debug("发出商品。。。");
                if (count == 0) {
                    currentState = SOLD_OUT;
                    LOGGER.debug("商品售罄");
                }else {
                    currentState = NO_INSERT;
                }
                break;
        }
    }
}
