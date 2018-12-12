package network.marmoj.transformer;

import network.marmoj.model.core.IntentTx;
import network.marmoj.model.request.IntentTxRequest;
import org.web3j.utils.Numeric;

import java.util.function.Function;

public class IntentTxRequestTransformer {

    final static Function<IntentTx, IntentTxRequest> function = intentTx -> {
        IntentTxRequest request = new IntentTxRequest();
        request.setData(Numeric.toHexString(intentTx.getData()));
        request.setMinGasLimit(intentTx.getMinGasLimit());
        request.setMaxGasPrice(intentTx.getMaxGasPrice());
        request.setTo(intentTx.getTo());
        request.setValue(intentTx.getValue());
        return request;
    };

    public static IntentTxRequest transform(IntentTx intentTx) {
        return function.apply(intentTx);
    }

}