package edu.eci.arsw.threads;

import java.util.LinkedList;
import edu.eci.arsw.spamkeywordsdatasource.HostBlacklistsDataSourceFacade;


public class BlackListSearchThread extends Thread {
  private int startIndex;
  private int endIndex;
  private String ipadress;
  private HostBlacklistsDataSourceFacade skds;
  LinkedList<Integer> blackListOccurrences;
  private int checkedCount;

  public BlackListSearchThread(
      int startIndex, int endIndex, String ipadress, HostBlacklistsDataSourceFacade skds) {
    this.startIndex = startIndex;
    this.endIndex = endIndex;
    this.ipadress = ipadress;
    this.skds = skds;
    this.blackListOccurrences = new LinkedList<>();
  }

  @Override
  public void run() {
    for (int i = startIndex; i < endIndex; i++) {
      checkedCount++;
      if (skds.isInBlackListServer(i, ipadress)) {
        blackListOccurrences.add(i);
      }
    }
  }

  public LinkedList<Integer> getBlackListOccurrences() {
    return blackListOccurrences;
  }

  public int getCheckedCount() {
    return checkedCount;
  }
}
