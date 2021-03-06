package org.freerealm.player;

/**
 *
 * @author Deniz ARIKAN
 */
public interface Message {

    public String getSubject();

    public StringBuffer getText();

    public boolean isRead();

    public void setRead(boolean read);

    public int getTurnSent();
}
