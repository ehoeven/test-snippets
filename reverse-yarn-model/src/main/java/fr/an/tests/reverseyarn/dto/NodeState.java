package fr.an.tests.reverseyarn.dto;

public enum NodeState {
  NEW, 
  RUNNING, 
  UNHEALTHY, 
  DECOMMISSIONED, 
  LOST, 
  REBOOTED,
  DECOMMISSIONING,
  SHUTDOWN;
}
