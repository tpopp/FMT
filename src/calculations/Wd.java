/**
 * 
 */
package calculations;

public class Wd {
	double n0, n1, n2, n3, nv1, nv2;

	public Wd(double a, double b, double c, double d, double e, double f) {
		n0 = a;
		n1 = b;
		n2 = c;
		n3 = d;
		nv1 = e;
		nv2 = f;
	}

	public void set_values(double n0, double n1, double n2, double n3,
			double nv1, double nv2) {
		this.n0 = n0;
		this.n1 = n1;
		this.n2 = n2;
		this.n3 = n3;
		this.nv1 = nv1;
		this.nv2 = nv2;
	}

	public double ret_n0() {
		return n0;
	}

	public double ret_n1() {
		return n1;
	}

	public double ret_n2() {
		return n2;
	}

	public double ret_n3() {
		return n3;
	}

	public double ret_nv1() {
		return nv1;
	}

	public double ret_nv2() {
		return nv2;
	}
};
