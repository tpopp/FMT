/**
 * 
 */
package calculations;

import java.lang.reflect.Array;

/**
 * @author Tres
 * 
 */
public class Util {


	double sign(double a, double b) {
		return b >= 0 ? (a >= 0 ? a : -a) : (a >= 0 ? -a : a);
	}

	void swap(double a, double b) {
		double dum = a;
		a = b;
		b = dum;
	}

	class Vec {
		int nn; // size of array. upper index is nn-1
		double[] v;

		public Vec() {
			nn = 0;
			v = null;
		}

		@SuppressWarnings("unchecked")
		public Vec(int n) {
			nn = n;
			v = new double[n];
		}

		public Vec(double a, int n) {
			nn = n;
			v = new double[n];
			for (int i = 0; i < n; i++) {
				v[i] = a;
			}
		}

		public Vec(double[] a, int n) {
			nn = n;
			v = new double[n];
			for (int i = 0; i < n; i++) {
				v[i] = a[i];
			}
		}

		public Vec(Vec rhs) {
			nn = rhs.nn;
			v = new double[nn];
			double[] arr = rhs.v;
			for (int i = 0; i < nn; i++) {
				v[i] = arr[i];
			}
		}

		Vec assign(Vec rhs)
		// postcondition: normal assignment via copying has been performed;
		// if vector and rhs were different sizes, vector
		// has been resized to match the size of rhs
		{
			if (this != rhs) {
				if (nn != rhs.nn) {
					nn = rhs.nn;
					v = new double[nn];
				}
				double[] arr = rhs.v;
				for (int i = 0; i < nn; i++)
					v[i] = arr[i];
			}
			return this;
		}

		Vec assign(double a) // assign a to every element
		{
			for (int i = 0; i < nn; i++)
				v[i] = a;
			return this;
		}

		Vec multiply(double a)
		// multiplying by a constant
		{
			for (int i = 0; i < nn; i++)
				v[i] *= a;
			return this;
		}

		@SuppressWarnings("unchecked")
		Vec add(Vec rhs)
		// adding element by element
		{
			if (this != rhs) {
				if (nn != rhs.nn) {
					nn = rhs.nn;
					v = new double[nn];
				}
				for (int i = 0; i < nn; i++)
					v[i] += rhs.get(i);
			}
			return this;
		}

		@SuppressWarnings("unchecked")
		Vec subtract(Vec rhs)
		// subtracting element by element
		{
			if (this != rhs) {
				if (nn != rhs.nn) {
					nn = rhs.nn;
					v = new double[nn];
				}
				for (int i = 0; i < nn; i++)
					v[i] -= rhs.v[i];
			}
			return this;
		}

		void resize(int newn) {
			nn = newn;
			v = nn > 0 ? new double[nn] : null;
		}

		double set(int i) // subscripting
		{
			return v[i];
		}

		double get(int i) // subscripting
		{
			return v[i];
		}

		int size() {
			return nn;
		}

		class Mat {
			int nn;
			int mm;
			double[][] v;

			public Mat() {
				nn = 0;
				mm = 0;
				v = null;
			}

			public Mat(int n, int m) {
				nn = n;
				mm = m;
				v = new double[n][m];
			}

			public Mat(double a, int n, int m) {
				v = new double[n][m];
				for (int i = 0; i < n; i++)
					for (int j = 0; j < m; j++)
						v[i][j] = a;
			}

			public Mat(double[] a, int n, int m) {
				nn = n;
				mm = m;
				v = new double[n][m];
				for (int i = 0; i < n; i++)
					for (int j = 0; j < m; j++)
						v[i][j] = a[i * m + j];
			}

			public Mat(Mat rhs) {
				int i, j;
				nn = rhs.nn;
				mm = rhs.mm;
				v = new double[nn][mm];
				for (i = 0; i < nn; i++)
					for (j = 0; j < mm; j++)
						v[i][j] = rhs.v[i][j];
			}

			Mat assign(Mat rhs)
			// postcondition: normal assignment via copying has been performed;
			// if matrix and rhs were different sizes, matrix
			// has been resized to match the size of rhs
			{
				if (!(this == rhs)) {
					int i, j;
					if (nn != rhs.nn || mm != rhs.mm) {
						nn = rhs.nn;
						mm = rhs.mm;
						v = new double[nn][mm];
					}
					for (i = 0; i < nn; i++)
						for (j = 0; j < mm; j++)
							v[i][j] = rhs.v[i][j];
				}
				return this;
			}

			Mat assign(double a) // assign a to every element
			{
				for (int i = 0; i < nn; i++)
					for (int j = 0; j < mm; j++)
						v[i][j] = a;
				return this;
			}

			double[] at(int i) // subscripting: pointer to row i
			{
				return v[i];
			}

			int nrows() {
				return nn;
			}

			int ncols() {
				return mm;
			}
		}
	}
}