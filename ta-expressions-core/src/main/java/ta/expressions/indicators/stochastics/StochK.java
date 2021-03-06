package ta.expressions.indicators.stochastics;

import ta.expressions.common.stats.HighestValue;
import ta.expressions.common.stats.LowestValue;
import ta.expressions.common.stats.SMA;
import ta.expressions.core.AnalyticFunction;
import ta.expressions.core.Constant;
import ta.expressions.core.NumericExpression;
import ta.expressions.core.TernaryOperation;
import ta.expressions.indicators.ParameterString;
import ta.expressions.indicators.variables.ClosePrice;

public class StochK extends AnalyticFunction {

	public static final String KEYWORD = "StochK";

	public static StochK fromString(String params) {
		ParameterString ps = new ParameterString(params);
		return new StochK(ps.intValue(0), ps.intValue(1));
	}

	private final NumericExpression formula;

	public StochK(int n) {
		this(n, 1);
	}

	public StochK(int n1, int n2) {
		super(functionRepresentation(KEYWORD, n1, n2));
		NumericExpression dividend = ClosePrice.INSTANCE.minus(LowestValue.lowestLow(n1)).multipliedBy(100);
		NumericExpression divisor = HighestValue.highestHigh(n1).minus(LowestValue.lowestLow(n1));
		NumericExpression raw = new TernaryOperation(
				divisor.equalTo(0), 
				Constant.valueOf(0), 
				dividend.dividedBy(divisor)
				);
		this.formula = n2 > 1 ? new SMA(raw, n2) : raw;
	}

	@Override
	protected NumericExpression getFormula() {
		return formula;
	}


}
