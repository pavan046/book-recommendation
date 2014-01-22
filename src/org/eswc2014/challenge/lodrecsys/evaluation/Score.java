/**
 * Score.java
 *
 * Stores the numeric score of an item given by a particular user.
 *
 * @version 1.0 (November 11, 2013)
 *
 * @author Ivan Cantador, Universidad Autonoma de Madrid, ivan.cantador@uam.es
 */
package org.eswc2014.challenge.lodrecsys.evaluation;

public class Score implements Comparable<Score> {

    private String userId;
    private String itemId;
    private double value;

    /**
     * Builds a Score instance storing its user, item and numeric value.
     *
     * @param userId the identifier of the user associated to the score
     * @param itemId the identifier of the item associated to the score
     * @param value the score's numeric value
     *
     * @throws IllegalArgumentException in case of a null user/item identifier
     */
    public Score(String userId, String itemId, double value) throws IllegalArgumentException {
        if (userId == null) {
            throw new IllegalArgumentException("null user id");
        }
        if (itemId == null) {
            throw new IllegalArgumentException("null item id");
        }

        this.userId = userId;
        this.itemId = itemId;
        this.value = value;
    }

    /**
     * Returns the identfier of the user associated to the score.
     *
     * @return the score's user identifier
     */
    public String getUserId() {
        return this.userId;
    }

    /**
     * Returns the identfier of the item associated to the score.
     *
     * @return the score's item identifier
     */
    public String getItemId() {
        return this.itemId;
    }

    /**
     * Returns the numeric value of the score.
     *
     * @return the score's numeric value
     */
    public double getValue() {
        return this.value;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + (this.userId != null ? this.userId.hashCode() : 0);
        hash = 67 * hash + (this.itemId != null ? this.itemId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Score other = (Score) obj;
        if ((this.userId == null) ? (other.userId != null) : !this.userId.equals(other.userId)) {
            return false;
        }
        if ((this.itemId == null) ? (other.itemId != null) : !this.itemId.equals(other.itemId)) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(Score t) {
        if (t == null) {
            return Integer.MIN_VALUE;
        }
        return (int) (t.getValue() - this.value);
    }

    @Override
    public String toString() {
        return "Score{" + "user=" + this.userId + ", item=" + this.itemId + ", value=" + this.value + '}';
    }
}
