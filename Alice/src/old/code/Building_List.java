package old.code;

import java.util.ArrayList;

import bwapi.Order;
import bwapi.Pair;
import bwapi.TilePosition;
import bwapi.Unit;
import bwapi.UnitType;
import bwapi.UpgradeType;
import bwta.BWTA;
import bwta.BaseLocation;
import bwta.Chokepoint;

public class Building_List {

	public static ArrayList<Building_List> productionlist = new ArrayList<Building_List>();
	public static int reservedGas;
	public static int reservedMinerals;
	private UnitType UnitTyp;
	private UpgradeType UpgradeType;
	private int countBuildingWorker;
	private Unit buildingWorker;
	public static int shouldAvaibleCountMakroHatchery;
	public static int count;

	// Erstellt eine Liste um den Einzelnen UnitType Produktionszahlen etc.
	// zuzuweisen
	public static ArrayList<Building_List> Unitlist = new ArrayList<Building_List>();
	public static Building_List Zerg_Spawning_Pool = new Building_List(UnitType.Zerg_Spawning_Pool, null);
	public static Building_List Zerg_Hatchery = new Building_List(UnitType.Zerg_Hatchery, null);
	public static Building_List Zerg_Creep_Colony = new Building_List(UnitType.Zerg_Creep_Colony, null);
	public static Building_List Zerg_Evolution_Chamber = new Building_List(UnitType.Zerg_Evolution_Chamber, null);
	public static Building_List Zerg_Extractor = new Building_List(UnitType.Zerg_Extractor, null);
	public static Building_List Zerg_Greater_Spire = new Building_List(UnitType.Zerg_Greater_Spire, null);
	public static Building_List Zerg_Hive = new Building_List(UnitType.Zerg_Hive, null);
	public static Building_List Zerg_Lair = new Building_List(UnitType.Zerg_Lair, null);
	public static Building_List Zerg_Nydus_Canal = new Building_List(UnitType.Zerg_Nydus_Canal, null);
	public static Building_List Zerg_Spire = new Building_List(UnitType.Zerg_Spire, null);
	public static Building_List Zerg_Spore_Colony = new Building_List(UnitType.Zerg_Spore_Colony, null);
	public static Building_List Zerg_Sunken_Colony = new Building_List(UnitType.Zerg_Sunken_Colony, null);
	public static Building_List Zerg_Ultralisk_Cavern = new Building_List(UnitType.Zerg_Ultralisk_Cavern, null);
	public static Building_List Zerg_Hydralisk_Den = new Building_List(UnitType.Zerg_Hydralisk_Den, null);
	public static Building_List Zerg_Defiler_Mound = new Building_List(UnitType.Zerg_Defiler_Mound, null);
	public static Building_List Zerg_Queens_Nest = new Building_List(UnitType.Zerg_Queens_Nest, null);
	public static Building_List Zerg_Defiler = new Building_List(UnitType.Zerg_Defiler, null);
	public static Building_List Zerg_Devourer = new Building_List(UnitType.Zerg_Devourer, null);
	public static Building_List Zerg_Drone = new Building_List(UnitType.Zerg_Drone, null);
	public static Building_List Zerg_Guardian = new Building_List(UnitType.Zerg_Guardian, null);
	public static Building_List Zerg_Hydralisk = new Building_List(UnitType.Zerg_Hydralisk, null);
	public static Building_List Zerg_Infested_Terran = new Building_List(UnitType.Zerg_Infested_Terran, null);
	public static Building_List Zerg_Lurker = new Building_List(UnitType.Zerg_Lurker, null);
	public static Building_List Zerg_Mutalisk = new Building_List(UnitType.Zerg_Mutalisk, null);
	public static Building_List Zerg_Overlord = new Building_List(UnitType.Zerg_Overlord, null);
	public static Building_List Zerg_Queen = new Building_List(UnitType.Zerg_Queen, null);
	public static Building_List Zerg_Scourge = new Building_List(UnitType.Zerg_Scourge, null);
	public static Building_List Zerg_Ultralisk = new Building_List(UnitType.Zerg_Ultralisk, null);
	public static Building_List Zerg_Zergling = new Building_List(UnitType.Zerg_Zergling, null);

	// Upgrades
	public static Building_List Adrenal_Glands = new Building_List(null, bwapi.UpgradeType.Adrenal_Glands);
	public static Building_List Grooved_Spines = new Building_List(null, bwapi.UpgradeType.Grooved_Spines);
	public static Building_List Metabolic_Boost = new Building_List(null, bwapi.UpgradeType.Metabolic_Boost);
	public static Building_List Zerg_Missile_Attacks = new Building_List(null, bwapi.UpgradeType.Zerg_Missile_Attacks);
	public static Building_List Zerg_Carapace = new Building_List(null, bwapi.UpgradeType.Zerg_Carapace);
	public static Building_List Pneumatized_Carapace = new Building_List(null, bwapi.UpgradeType.Pneumatized_Carapace);
	public static Building_List Muscular_Augments = new Building_List(null, bwapi.UpgradeType.Muscular_Augments);

	public Building_List(UnitType UnitTyp, UpgradeType UpgradeType) {
		this.UnitTyp = UnitTyp;
		this.UpgradeType = UpgradeType;
	}

	public static void buildUnit() {
		// reserviert für den wichtigsten Bauauftrag Ressourcen
		if (productionlist.get(0).UnitTyp != null) {
			reservedMinerals = productionlist.get(0).UnitTyp.mineralPrice();
			reservedGas = productionlist.get(0).UnitTyp.gasPrice();
		}
		if (productionlist.get(0).UpgradeType != null) {
			reservedMinerals = productionlist.get(0).UpgradeType.mineralPrice();
			reservedGas = productionlist.get(0).UpgradeType.gasPrice();
		}

		// Baue die aktuelle Bauliste ab
		äussereschleife: for (Building_List thisTyp : productionlist) {
			UnitType productnow = thisTyp.UnitTyp;
			UpgradeType upgradenow = thisTyp.UpgradeType;
			if (upgradenow != null) {
				if (((upgradenow.gasPrice() + reservedGas <= Alice.self.gas() || upgradenow.gasPrice() == 0)
						&& (upgradenow.mineralPrice() + reservedMinerals <= Alice.self.minerals()
								|| upgradenow.mineralPrice() == 0))
						|| (upgradenow.equals(productionlist.get(0).UpgradeType)
								&& upgradenow.gasPrice() <= Alice.self.gas()
								&& upgradenow.mineralPrice() <= Alice.self.minerals())) {
					for (Unit myUnit : Alice.myBuildings) {
						/*
						 * if (myUnit.getType().equals(upgradenow.whatUpgrades()) &&
						 * myUnit.isUpgrading() && myUnit.getUpgrade().equals(upgradenow)) {
						 * productionlist.remove(thisTyp); // System.out.println("remove " +
						 * thisTyp.UpgradeType.toString()); break äussereschleife;
						 * 
						 * } else
						 */if (myUnit.getType().equals(upgradenow.whatUpgrades()) && myUnit.isCompleted()
								&& myUnit.isIdle()) {
							myUnit.upgrade(upgradenow);
							productionlist.remove(thisTyp);
							break äussereschleife;
						}
					}
				}
			}

			if (productnow != null) {

				// sucht von productnow die Parameter heraus und schaut ob noch Einheiten gebaut
				// werden sollen und ob genug Ressourcen vorhanden sind

				// prüft ob genug Ressourcen vorhanden sind
				if (((Alice.self.minerals() >= productnow.mineralPrice() + reservedMinerals
						|| productnow.mineralPrice() == 0)
						&& (Alice.self.gas() >= productnow.gasPrice() + reservedGas || productnow.gasPrice() == 0))
						|| (productnow.equals(productionlist.get(0).UnitTyp)
								&& Alice.self.minerals() >= productnow.mineralPrice()
								&& Alice.self.gas() >= productnow.gasPrice())) {
					// wenn eine Einheit gemorpht werden soll
					if (!productnow.isBuilding()) {

						for (Unit myUnit : Alice.self.getUnits()) {
							if (myUnit.getType() != UnitType.Zerg_Larva)
								continue;
							// baut nur, wenn genug Versorgung da ist
							if (productnow.supplyRequired() + Alice.supplyUsed() <= Alice.self.supplyTotal()
									|| productnow.equals(UnitType.Zerg_Overlord)) {
								myUnit.morph(productnow);
								productionlist.remove(thisTyp);
								// myList.addproduction--;
								// productionlist.remove(thisTyp);
								break äussereschleife;

							} else
								continue äussereschleife;
						}
					}

					// wenn ein Gebäude gebaut werden soll
					else {
						// sollte etwas gebaut werden wofür keine Drone/Larve gebraucht wird
						if (productnow.equals(UnitType.Zerg_Lair)) {
							// sucht die Hatchery in StartLocation
							for (Unit mainHatchery : Alice.myBuildings) {
								if (mainHatchery.getType().equals(UnitType.Zerg_Hatchery)
										&& mainHatchery.getPosition()
												.equals(BWTA.getStartLocation(Alice.self).getPosition())
										&& Alice.self.minerals() >= productnow.mineralPrice() + reservedMinerals
										&& Alice.self.gas() >= productnow.gasPrice() + reservedGas) {
									mainHatchery.morph(productnow);
									break äussereschleife;
								}
							}
						} else if (productnow.equals(UnitType.Zerg_Sunken_Colony)) {
							for (Unit creepColony : Alice.myBuildings) {
								if (creepColony.getType().equals(UnitType.Zerg_Creep_Colony)
										&& creepColony.isCompleted()
										&& Alice.self.minerals() >= productnow.mineralPrice() + reservedMinerals
										&& Alice.self.gas() >= productnow.gasPrice() + reservedGas) {
									creepColony.morph(productnow);
									break äussereschleife;
								}
							}
						}

						if (thisTyp.countBuildingWorker < countUnitProductionlist(productnow)
								&& productnow.whatBuilds().first.equals(UnitType.Zerg_Drone)) {
							// sucht einen Arbeiter
							for (Unit myUnit : Alice.myWorkers) {
								// prüft ob eine Drone dieses Gebäude nicht schon bauen möchte
								if (!(workerContainedProductionList(myUnit))) {

									// wenn eine Hatchery gebaut werden soll
									if (productnow == UnitType.Zerg_Hatchery) {
										if (shouldAvaibleCountMakroHatchery <= getCountMakroHatchery()) {
											TilePosition temp = Alice.nextExpandPosition();
											// wenn der Platz sichtbar ist
											if (temp != null) {
												if (!(Alice.game.isVisible(temp))) {
													myUnit.move(temp.toPosition());
													continue äussereschleife;
												} else {
													myUnit.build(productnow, temp);
													thisTyp.countBuildingWorker++;
													thisTyp.buildingWorker = myUnit;
													continue äussereschleife;
													// Alice.game.sendText("Drone will expandieren nach " +
													// temp.toPosition().toString());
												}
											}
											if (temp == null) {
												Alice.game.sendText("Drone findet keinen expandplatz");
												productionlist.remove(thisTyp);
											}
											continue äussereschleife;
										}
									}
									if (productnow.equals(UnitType.Zerg_Creep_Colony)) {
										for (Unit baseHatch : Alice.myBuildings) {
											if (baseHatch.getType().equals(UnitType.Zerg_Hatchery)) {
												for (BaseLocation thisBaseLocation : bwta.BWTA.getBaseLocations()) {
													if (thisBaseLocation.getTilePosition()
															.equals(baseHatch.getTilePosition())) {
														// Alice.game.sendText("BaseHatch gefunden");
														int countColonys = 0;
														for (Unit colony : Alice.myBuildings) {
															if ((colony.getType().equals(UnitType.Zerg_Creep_Colony)
																	|| colony.getType()
																			.equals(UnitType.Zerg_Sunken_Colony))
																	&& colony.getDistance(baseHatch) < 500) {
																countColonys++;
															}
														}
														if (countColonys < 3 && !(thisBaseLocation.isStartLocation())
																&& (Alice.self.allUnitCount(UnitType.Zerg_Creep_Colony)
																		+ Alice.self.allUnitCount(
																				UnitType.Zerg_Sunken_Colony)) > 0) {
															TilePosition buildTile = Alice.game.getBuildLocation(
																	productnow, baseHatch.getTilePosition());

															myUnit.build(productnow, buildTile);
															thisTyp.countBuildingWorker++;
															thisTyp.buildingWorker = myUnit;
															Alice.game.drawCircleMap(buildTile.toPosition(), 10,
																	bwapi.Color.White);
															continue äussereschleife;
														} else if (countColonys < 1
																&& thisBaseLocation.isStartLocation()) {
															TilePosition buildTile = Alice.game.getBuildLocation(
																	productnow, baseHatch.getTilePosition());
															myUnit.build(productnow, buildTile);
															thisTyp.countBuildingWorker++;
															thisTyp.buildingWorker = myUnit;
															// Alice.game.sendText("Drone bau Colony");
															Alice.game.drawCircleMap(buildTile.toPosition(), 10,
																	bwapi.Color.White);
															continue äussereschleife;
														}
													}
												}
											}
										}
										// if(getCountBaseHatchery() ==2) {

										// }
									}
									// TilePosition buildTile = getClosestBuildTile(productnow);
									TilePosition buildTile = Alice.game.getBuildLocation(productnow,
											Alice.self.getStartLocation());
									// TilePosition buildTile = getBuildTile(myUnit, productnow,
									// Alice.self.getStartLocation());
									// alle anderen Gebäude
									if (buildTile != null) {
										myUnit.build(productnow, buildTile);
										thisTyp.countBuildingWorker++;
										thisTyp.buildingWorker = myUnit;
										continue äussereschleife;
									} else
										continue äussereschleife;
								}
							}
						}
					}
				}
			}
		}
	}

	// Algoritmus der Baureinfolge / Prioritätensetzung
	public static void addBuildingQue() {

		// Füge Gebäude nach Zeitplan hinzu

		if (Alice.supplyUsed() == 9 * 2 && !(productionlist.contains(Zerg_Extractor))
				&& Alice.self.allUnitCount(UnitType.Zerg_Extractor) == 0
				&& Alice.self.allUnitCount(UnitType.Zerg_Hatchery) < 3 && Alice.self.minerals() >= 80) {
			addPriorityBauliste(Zerg_Extractor);
		}
		if (Alice.supplyUsed() == 9 * 2 && Alice.self.allUnitCount(UnitType.Zerg_Extractor) == 1
				&& ((Alice.self.allUnitCount(UnitType.Zerg_Egg) == 2
						&& Alice.self.allUnitCount(UnitType.Zerg_Drone) == 8
						&& Alice.self.allUnitCount(UnitType.Zerg_Hatchery) == 2)
						|| (Alice.self.allUnitCount(UnitType.Zerg_Egg) == 1
								&& Alice.self.allUnitCount(UnitType.Zerg_Drone) == 8
								&& Alice.self.allUnitCount(UnitType.Zerg_Hatchery) == 1))) {
			for (Unit extractor : Alice.myBuildings) {
				if (extractor.getType().equals(UnitType.Zerg_Extractor)) {
					extractor.cancelConstruction();
					Alice.supplyUsed += UnitType.Zerg_Drone.supplyRequired();
					break;
				}
			}
		}

		if (Alice.supplyUsed() == 10 * 2 && !(productionlist.contains(Zerg_Hatchery)) && countHatcheryTotal() == 1) {
			shouldAvaibleCountMakroHatchery++;
			addPriorityBauliste(Zerg_Hatchery);
		}
		if (Alice.self.allUnitCount(UnitType.Zerg_Hatchery) == 2 && !(productionlist.contains(Zerg_Overlord))
				&& (Building_List.countMorphing(UnitType.Zerg_Overlord)
						+ Alice.self.allUnitCount(UnitType.Zerg_Overlord)) < 2) {
			addPriorityBauliste(Zerg_Overlord);
		}
		if (Alice.supplyUsed() == 10 * 2 && !(productionlist.contains(Zerg_Spawning_Pool))
				&& Alice.self.allUnitCount(UnitType.Zerg_Spawning_Pool) == 0
				&& Alice.self.allUnitCount(UnitType.Zerg_Hatchery) == 2) {
			addPriorityBauliste(Zerg_Spawning_Pool);

		}
		if (Alice.supplyUsed() >= 14 * 2 && !(productionlist.contains(Zerg_Creep_Colony))
				&& (Alice.self.allUnitCount(UnitType.Zerg_Creep_Colony)
						+ Alice.self.allUnitCount(UnitType.Zerg_Sunken_Colony)) == 0) {
			addPriorityBauliste(Zerg_Creep_Colony);
		}
		if (Alice.supplyUsed() >= 15 * 2 && !(productionlist.contains(Zerg_Overlord))
				&& (Building_List.countMorphing(UnitType.Zerg_Overlord)
						+ Alice.self.allUnitCount(UnitType.Zerg_Overlord)) < 3) {
			addPriorityBauliste(Zerg_Overlord);
		}

		if (Alice.self.allUnitCount(UnitType.Zerg_Spawning_Pool) == 1 && !(productionlist.contains(Zerg_Sunken_Colony))
				&& (Alice.self.allUnitCount(UnitType.Zerg_Creep_Colony) == 1)
				&& (Alice.self.allUnitCount(UnitType.Zerg_Sunken_Colony) == 0)) {
			for (Unit thisUnit : Alice.myBuildings) {
				if (thisUnit.getType().equals(UnitType.Zerg_Spawning_Pool) && thisUnit.isCompleted()) {
					for (int i = 0; i < 5; i++) {
						addPriorityBauliste(Zerg_Zergling);
					}
					addPriorityBauliste(Zerg_Sunken_Colony);
					break;
				}
			}
		}

		if (Alice.supplyUsed() >= 15 * 2 && !(productionlist.contains(Zerg_Hatchery)) && countHatcheryTotal() == 2) {
			addPriorityBauliste(Zerg_Hatchery);

		}
		if (Alice.self.allUnitCount(UnitType.Zerg_Hatchery) == 3 && !(productionlist.contains(Zerg_Extractor))
				&& Alice.self.allUnitCount(UnitType.Zerg_Extractor) == 0) {
			addPriorityBauliste(Zerg_Extractor);
		}
		if (Alice.supplyUsed() >= 24 * 2 && !(productionlist.contains(Zerg_Overlord))
				&& (Building_List.countMorphing(UnitType.Zerg_Overlord)
						+ Alice.self.allUnitCount(UnitType.Zerg_Overlord)) < 4) {
			addPriorityBauliste(Zerg_Overlord);
		}
		if (Alice.supplyUsed() >= 23 * 2 && !(productionlist.contains(Zerg_Hydralisk_Den))
				&& Alice.self.allUnitCount(UnitType.Zerg_Hydralisk_Den) == 0) {
			addPriorityBauliste(Zerg_Hydralisk_Den);
			for (int i = 0; i < 4; i++) {
				addPriorityBauliste(Zerg_Zergling);
			}
		}
		if (!(productionlist.contains(Metabolic_Boost)) && Alice.self.getUpgradeLevel(Metabolic_Boost.UpgradeType) == 0
				&& !(Alice.self.isUpgrading(Metabolic_Boost.UpgradeType))
				&& Alice.self.allUnitCount(UnitType.Zerg_Hydralisk_Den) > 0) {
			addPriorityBauliste(Metabolic_Boost);
			// System.out.println(count++ + " count metabolic boost");
		}

		if (Alice.supplyUsed() >= 29 * 2 && !(productionlist.contains(Zerg_Creep_Colony))
				&& (Alice.self.allUnitCount(UnitType.Zerg_Creep_Colony)
						+ Alice.self.allUnitCount(UnitType.Zerg_Sunken_Colony)) == 1
				&& Alice.self.allUnitCount(UnitType.Zerg_Hydralisk_Den) > 0
				&& countFinishedBuilding(UnitType.Zerg_Hatchery) >= 3) {
			addPriorityBauliste(Zerg_Sunken_Colony);
			addPriorityBauliste(Zerg_Sunken_Colony);
			addPriorityBauliste(Zerg_Creep_Colony);
			addPriorityBauliste(Zerg_Creep_Colony);
		}
		if (Alice.supplyUsed() >= 30 * 2 && !(productionlist.contains(Zerg_Overlord))
				&& (Building_List.countMorphing(UnitType.Zerg_Overlord)
						+ Alice.self.allUnitCount(UnitType.Zerg_Overlord)) < 5) {
			addPriorityBauliste(Zerg_Overlord);
			// System.out.println("add 4. OV");
		}
		if (Alice.supplyUsed() >= 32 * 2 && !(productionlist.contains(Zerg_Evolution_Chamber))
				&& Alice.self.allUnitCount(UnitType.Zerg_Evolution_Chamber) == 0) {
			addPriorityBauliste(Zerg_Evolution_Chamber);
			addPriorityBauliste(Zerg_Evolution_Chamber);
		}
		if (Alice.supplyUsed() >= 35 * 2 && !(productionlist.contains(Zerg_Hatchery)) && countHatcheryTotal() == 3
				&& Alice.self.allUnitCount(UnitType.Zerg_Evolution_Chamber) == 2) {
			shouldAvaibleCountMakroHatchery++;
			// System.out.println("add 4. Hatch");
			addPriorityBauliste(Zerg_Hatchery);
		}
		if ((Alice.self.isUpgrading(bwapi.UpgradeType.Metabolic_Boost)
				|| Alice.self.getUpgradeLevel(bwapi.UpgradeType.Metabolic_Boost) == 1)
				&& Alice.self.getUpgradeLevel(bwapi.UpgradeType.Grooved_Spines) == 0
				&& !(Alice.self.isUpgrading(bwapi.UpgradeType.Grooved_Spines))
				&& !(productionlist.contains(Grooved_Spines))
				&& Alice.self.allUnitCount(UnitType.Zerg_Sunken_Colony) == 3) {
			addPriorityBauliste(Grooved_Spines);
		}
		if ((Alice.self.isUpgrading(bwapi.UpgradeType.Grooved_Spines)
				|| Alice.self.getUpgradeLevel(bwapi.UpgradeType.Grooved_Spines) == 1)
				&& Alice.self.getUpgradeLevel(bwapi.UpgradeType.Zerg_Missile_Attacks) == 0
				&& !(Alice.self.isUpgrading(bwapi.UpgradeType.Zerg_Missile_Attacks))
				&& !(productionlist.contains(Zerg_Missile_Attacks))
				&& Alice.self.allUnitCount(UnitType.Zerg_Evolution_Chamber) > 0) {
			addPriorityBauliste(Zerg_Missile_Attacks);
		}
		if ((Alice.self.isUpgrading(bwapi.UpgradeType.Zerg_Missile_Attacks)
				|| Alice.self.getUpgradeLevel(bwapi.UpgradeType.Zerg_Missile_Attacks) == 1)
				&& Alice.self.getUpgradeLevel(bwapi.UpgradeType.Zerg_Carapace) == 0
				&& !(Alice.self.isUpgrading(bwapi.UpgradeType.Zerg_Carapace))
				&& !(productionlist.contains(Zerg_Carapace))
				&& Alice.self.allUnitCount(UnitType.Zerg_Evolution_Chamber) > 1) {
			addPriorityBauliste(Zerg_Carapace);
		}
		if ((Alice.self.isUpgrading(bwapi.UpgradeType.Zerg_Carapace)
				|| Alice.self.getUpgradeLevel(bwapi.UpgradeType.Zerg_Carapace) == 1)
				&& Alice.self.getUpgradeLevel(bwapi.UpgradeType.Muscular_Augments) == 0
				&& !(Alice.self.isUpgrading(bwapi.UpgradeType.Muscular_Augments))
				&& !(productionlist.contains(Muscular_Augments))) {
			addPriorityBauliste(Muscular_Augments);
		}
		// System.out.println("start enemyunit>myunits");
		if (Alice.scoreEnemyUnits > Alice.scoreMyUnits) {
			UnitType nextUnitType = null;
			if (Alice.self.gas() >= UnitType.Zerg_Hydralisk.gasPrice()
					&& countFinishedBuilding(UnitType.Zerg_Hydralisk_Den) > 0
					&& countUnitProductionlist(UnitType.Zerg_Hydralisk) < 3) {
				addPriorityBauliste(Zerg_Hydralisk);
				// System.out.println("add extra hydra");
				nextUnitType = UnitType.Zerg_Hydralisk;
			} else if (Alice.self.gas() < UnitType.Zerg_Hydralisk.gasPrice()
					&& countFinishedBuilding(UnitType.Zerg_Spawning_Pool) > 0
					&& countUnitProductionlist(UnitType.Zerg_Zergling) < 3) {
				addPriorityBauliste(Zerg_Zergling);
				nextUnitType = UnitType.Zerg_Zergling;
				// System.out.println("add extra ling");
			}
			// System.out.println("start set");

			if (nextUnitType != null && (productionlist.get(0).UpgradeType != null
					|| (productionlist.get(0).UnitTyp != null && !(productionlist.get(0).UnitTyp.equals(nextUnitType))))
					&& !(productionlist.get(0).UnitTyp.equals(UnitType.Zerg_Overlord))) {
				Building_List first = productionlist.get(0);
				Building_List next;
				for (Building_List thisList : productionlist) {
					if (thisList.UnitTyp.equals(nextUnitType)) {
						next = thisList;
						productionlist.set(0, next);
						productionlist.set(productionlist.indexOf(thisList), first);
						break;
					}
				}
			}
		}

		if (Alice.scoutForce.size() == 0 && !productionlist.contains(Zerg_Zergling)
				&& countMorphing(UnitType.Zerg_Zergling) == 0 && countUnFinishedBuilding(UnitType.Zerg_Spawning_Pool)>=1) {
			addPriorityBauliste(Zerg_Zergling);
		} 

		// System.out.println("start makroHatch");
		if (countHatcheryTotal() >= 4
				&& Alice.self.minerals() >= 500 + countUnFinishedBuilding(UnitType.Zerg_Hatchery) * 100
				&& !(productionlist.contains(Zerg_Hatchery))) {
			addPriorityBauliste(Zerg_Hatchery);
			shouldAvaibleCountMakroHatchery++;
			// System.out.println("adde extra hatch, dronecount: " +
			// Alice.self.allUnitCount(UnitType.Zerg_Drone));
		}
		// System.out.println("ende makrohatch");
		// füge Units hinzu die keine besondere Priorität haben und Platz frei ist
		if (productionlist.size() < 10) {
			if (Alice.self.allUnitCount(UnitType.Zerg_Overlord) >= 5
					&& Alice.supplyUsed + 10 >= (Alice.self.supplyTotal()
							+ (countMorphing(UnitType.Zerg_Overlord) + countUnitProductionlist(UnitType.Zerg_Overlord))
									* UnitType.Zerg_Overlord.supplyProvided())
					&& !(productionlist.contains(Zerg_Overlord))) {
				int x = (Alice.self.supplyTotal()
						+ (countMorphing(UnitType.Zerg_Overlord) + countUnitProductionlist(UnitType.Zerg_Overlord))
								* UnitType.Zerg_Overlord.supplyProvided());
				int y = Alice.supplyUsed() + 10;
				// System.out.println("add extra ov");

				addPriorityBauliste(Zerg_Overlord);
			} /*
				 * else if (Alice.self.minerals() >= 300 &&
				 * !(productionlist.contains(Zerg_Hatchery)) &&
				 * Alice.self.allUnitCount(UnitType.Zerg_Hatchery) >= 4 &&
				 * Alice.nextExpandPosition() != null && Alice.possibleExpandplace.size() != 0)
				 * { addPriorityBauliste(Zerg_Hatchery);
				 * 
				 * }
				 */
			// Wenn keine Unit besondere Priorität hat
			else if (Alice.self.allUnitCount(UnitType.Zerg_Drone) + countUnitProductionlist(UnitType.Zerg_Drone) < Alice
					.countTotalMineralworker() && Alice.scoreMyUnits >= Alice.scoreEnemyUnits) {
				// System.out.println("count Drones " +
				// Alice.self.allUnitCount(UnitType.Zerg_Drone) + " sollte bis supply: "
				// + Alice.self.supplyUsed() / 2 * 0.5);
				addBauliste(Zerg_Drone);
			} else if (Alice.self.gas() >= UnitType.Zerg_Hydralisk.gasPrice() * 3) {
				addBauliste(Zerg_Hydralisk);
			} else if (Alice.self.allUnitCount(UnitType.Zerg_Spawning_Pool) > 0) {
				addBauliste(Zerg_Zergling);
			}
		}
	}

	// fügt der Bauliste das Gebäude hinzu und erhöht die Anzahl der Produktion
	public static void addPriorityBauliste(Building_List building) {
		productionlist.add(0, building);

	}

	// wenn die Unit besondere Priorittät hat
	public static void addBauliste(Building_List building) {
		if (productionlist.size() > 10) {
			productionlist.add(3, building);
		} else
			productionlist.add(building);
		if (productionlist.size() > 10)
			productionlist.remove(productionlist.size() - 1);
	}

	public static void checkProduction() {
		// System.out.println("start 1");
		// wenn eine Drone den Bauauftrag nicht mehr hat, Platz freimachen
		if (Alice.game.getFrameCount() % 10 == 0) {
			for (Building_List thisList : Building_List.productionlist) {
				if (thisList.getBuildingWorker() != null) {
					if (!(thisList.getBuildingWorker().getOrder().equals(Order.PlaceBuilding))) {
						thisList.removeBuildingWorker();
					}
				}
			}
		}
		// System.out.println("start 2");
		// Overlords haben Priorität
		if (productionlist.get(0).UnitTyp != null && !(productionlist.get(0).UnitTyp.equals(UnitType.Zerg_Overlord))
				&& productionlist.contains(Zerg_Overlord) && productionlist.get(0) != null) {
			// System.out.println("test 1");
			for (Building_List thisTyp : productionlist) {
				if (thisTyp.UnitTyp != null && thisTyp.UnitTyp.equals(UnitType.Zerg_Overlord)) {
					// System.out.println("test 2");
					Building_List temp = productionlist.get(0);
					int index = productionlist.indexOf(thisTyp);
					productionlist.set(0, thisTyp);
					productionlist.set(index, temp);
					break;
				}
			}
		}
		// System.out.println("start 3");
		// sorgt dafür dass Gebäude immer Priorität haben
		if (productionlist.get(0).UnitTyp != null && !(productionlist.get(0).UnitTyp.isBuilding())
				&& productionlist.get(0) != null && !(productionlist.get(0).UnitTyp.equals(UnitType.Zerg_Overlord))
				&& Alice.scoreEnemyUnits <= Alice.scoreMyUnits) {
			for (Building_List thisTyp : productionlist) {

				if (thisTyp.equals(Zerg_Sunken_Colony) && productionlist.contains(Zerg_Creep_Colony)) {
					continue;
				}

				if (thisTyp.UnitTyp != null && thisTyp.UnitTyp.isBuilding()
						&& !(thisTyp.equals(productionlist.get(0)))) {
					Building_List temp = productionlist.get(0);
					int index = productionlist.indexOf(thisTyp);
					productionlist.set(0, thisTyp);
					productionlist.set(index, temp);
					break;
				}
			}
		}
		// System.out.println("start 4");
		// AttackUnits haben Prioriät wenn myScore<enemyScore und kein OV gebraucht wird
		if (Alice.scoreEnemyUnits > Alice.scoreMyUnits && Alice.self.supplyTotal() > Alice.supplyUsed + 2
				&& productionlist.get(0).UpgradeType != null
				|| (productionlist.get(0).UnitTyp != null
						&& !(productionlist.get(0).UnitTyp.equals(UnitType.Zerg_Zergling)))) {
			for (Building_List thisTyp : productionlist) {
				if (thisTyp.UnitTyp != null && thisTyp.UnitTyp.equals(UnitType.Zerg_Zergling)
						&& !(thisTyp.equals(productionlist.get(0)))) {
					Building_List temp = productionlist.get(0);
					int index = productionlist.indexOf(thisTyp);
					productionlist.set(0, thisTyp);
					productionlist.set(index, temp);
					break;
				}
			}
		}

		/*
		 * äussereschleife: for (Building_List thisTyp : productionlist) { UnitType
		 * productnow = thisTyp.UnitTyp;
		 * 
		 * if (productnow != null) { // für Leistung werden nur Gebäude überprüft if
		 * (!(productnow.isBuilding()) ||
		 * productnow.equals(UnitType.Zerg_Sunken_Colony)) { continue; }
		 * 
		 * // zählt wie oft die Einheit vorhanden ist und wie oft sie gebaut wird int
		 * countStore = 0; int countConstrukting = 0; for (Unit currentUnit :
		 * Alice.self.getUnits()) {
		 * 
		 * // zählt für Einheiten if (!(productnow.isBuilding())) { countConstrukting =
		 * countMorphing(productnow); if (currentUnit.getType() == productnow) {
		 * countStore++; } }
		 * 
		 * // zählt für Gebäude else if (currentUnit.getType() == productnow) {
		 * countStore++; } }
		 * 
		 * // zählt wie oft produktnow bereits gestorben ist int deadUnitCount =
		 * Alice.self.deadUnitCount(productnow);
		 * 
		 * // zählt wieviele Drohnen in Gebäude eingesetzt wurden int usedDrones = 0; if
		 * (productnow == UnitType.Zerg_Drone) { for (Unit myUnit :
		 * Alice.self.getUnits()) { if (myUnit.getType().isBuilding()) usedDrones++; }
		 * // für die Hatchery am Anfang usedDrones--; } // bestimmt die addproduktion
		 * der momentanen Produktion for (Building_List UnitTypes : Unitlist) { if
		 * (UnitTypes.UnitTyp == productnow) { int lastaddproduction =
		 * UnitTypes.addproduction; UnitTypes.addproduction = UnitTypes.shouldAvaible -
		 * countConstrukting - countStore - deadUnitCount - usedDrones;
		 * 
		 * break; } } } }
		 */
	}

	// zählt wieviele MakroHatcherys momentan exisiteren
	public static int getCountMakroHatchery() {
		int countBaseHatchery = getCountBaseHatchery();
		int countMakroHatchery;

		countMakroHatchery = (Alice.self.allUnitCount(UnitType.Zerg_Hatchery)
				+ Alice.self.allUnitCount(UnitType.Zerg_Lair) + Alice.self.allUnitCount(UnitType.Zerg_Hive))
				- countBaseHatchery;
		return countMakroHatchery;
	}

	public static int getCountBaseHatchery() {
		int countBaseHatchery = 0;
		äussereschleife: for (Unit myUnit : Alice.myBuildings) {
			if (myUnit.getType() == UnitType.Zerg_Hatchery || myUnit.getType().equals(UnitType.Zerg_Lair)
					|| myUnit.getType().equals(UnitType.Zerg_Hive)) {
				// zählt wieviele BaseHatcherys es gibt
				for (BaseLocation Location : BWTA.getBaseLocations()) {
					if (myUnit.getTilePosition().equals(Location.getTilePosition())) {
						countBaseHatchery++;
						continue äussereschleife;
					}

				}
			}
		}
		return countBaseHatchery;
	}


	public static int countFinishedBuilding(UnitType building) {
		int count = 0;
		for (Unit thisUnit : Alice.myBuildings) {
			if (thisUnit.getType().equals(building) && !thisUnit.isBeingConstructed()) {
				count++;
			}
		}
		return count;
	}

	public static int countUnFinishedBuilding(UnitType building) {
		int count = 0;
		for (Unit thisUnit : Alice.myBuildings) {
			if (thisUnit.getType().equals(building) && thisUnit.isBeingConstructed()) {
				count++;
			}
		}
		return count;
	}

	public static int countHatcheryTotal() {
		return Alice.self.allUnitCount(UnitType.Zerg_Hatchery) + Alice.self.allUnitCount(UnitType.Zerg_Lair)
				+ Alice.self.allUnitCount(UnitType.Zerg_Hive);
	}

	public static Building_List getUnit(UnitType zergUnit) {
		Building_List retUnit = null;

		for (Building_List thisUnit : Unitlist) {
			if (thisUnit.UnitTyp.equals(zergUnit)) {
				retUnit = thisUnit;
				break;
			}
		}
		return retUnit;
	}

	// initialisiert das Array Unit
	public static void initiateGebäude() {
		Unitlist.add(Zerg_Spawning_Pool);
		Unitlist.add(Zerg_Extractor);
		Unitlist.add(Zerg_Hatchery);
		Unitlist.add(Zerg_Creep_Colony);
		Unitlist.add(Zerg_Evolution_Chamber);
		Unitlist.add(Zerg_Greater_Spire);
		Unitlist.add(Zerg_Hive);
		Unitlist.add(Zerg_Lair);
		Unitlist.add(Zerg_Nydus_Canal);
		Unitlist.add(Zerg_Spire);
		Unitlist.add(Zerg_Spore_Colony);
		Unitlist.add(Zerg_Sunken_Colony);
		Unitlist.add(Zerg_Ultralisk_Cavern);
		Unitlist.add(Zerg_Hydralisk_Den);
		Unitlist.add(Zerg_Queens_Nest);
		Unitlist.add(Zerg_Defiler);
		Unitlist.add(Zerg_Devourer);
		Unitlist.add(Zerg_Drone);
		Unitlist.add(Zerg_Guardian);
		Unitlist.add(Zerg_Hydralisk);
		Unitlist.add(Zerg_Infested_Terran);
		Unitlist.add(Zerg_Lurker);
		Unitlist.add(Zerg_Mutalisk);
		Unitlist.add(Zerg_Overlord);
		Unitlist.add(Zerg_Queen);
		Unitlist.add(Zerg_Scourge);
		Unitlist.add(Zerg_Ultralisk);
		Unitlist.add(Zerg_Zergling);
	}

	// zählt wie oft die Einheit gerade produziert wird
	public static int countMorphing(UnitType Typ) {
		int count = 0;
		for (Unit thisUnit : Alice.self.getUnits()) {
			if (thisUnit.getType() == UnitType.Zerg_Egg && thisUnit.getBuildType() == Typ) {
				count++;
			}
		}
		return count;
	}

	// Getters & Setters

	public UnitType getUnitType() {
		return this.UnitTyp;
	}

	public UpgradeType getUpgradeType() {
		return this.UpgradeType;
	}

	public int getcountBuildingWorker() {
		return this.countBuildingWorker;
	}

	public void decreaseCountBuildingWorker() {
		countBuildingWorker--;
	}

	public Unit getBuildingWorker() {
		return this.buildingWorker;
	}

	public void removeBuildingWorker() {
		this.buildingWorker = null;
		decreaseCountBuildingWorker();
	}

	public static boolean workerContainedProductionList(Unit thisUnit) {
		for (Building_List thisList : productionlist) {
			if (thisList.buildingWorker != null && thisList.buildingWorker.equals(thisUnit)) {
				return true;
			}
		}
		return false;
	}

	public static int countUnitProductionlist(UnitType type) {
		int count = 0;
		for (Building_List thisList : productionlist) {
			if (thisList.UnitTyp != null && thisList.UnitTyp.equals(type)) {
				count++;
			}
		}
		return count;
	}

}
