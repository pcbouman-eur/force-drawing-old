package nl.pcbouman.force_drawing_old;

/**
 * Simple 4x4 Matrix Class, for Rotation Purposes
 * @author Paul Bouman
 *
 */

public class Mat4D {
	
	/**
	 * The Matrix itself
	 */
	public double[][] mat = new double[4][4];

	/**
	 * Calculates the product of this matrix and a vector
	 * @param vec The vector that we should multiply the matrix with
	 * @return the resulting vector
	 */
	public double[] prod(double[] vec) {
		double[] result = new double[4];
		for (int x = 0; x < 4; x++)
			for (int y = 0; y < 4; y++) {
				result[x] += mat[x][y] * vec[y];
			}
		return result;
	}

	/**
	 * Transforms the coordinates vector of a node with this matrix
	 * @param n the node to be transformed
	 */
	public void transform(Node n) {
		double[] vec = new double[4];
		for (int t = 0; t < 3; t++)
			vec[t] = n.coors[t];
		vec[3] = 1;

		double[] trans = prod(vec);

		for (int t = 0; t < 3; t++)
			n.coors[t] = trans[t] / trans[3];

	}
}
