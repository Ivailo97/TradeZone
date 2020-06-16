import { Town } from "./town";
import { TownInRegion } from "./town-in-region";

export class Region {
    name: string;
    totalAds: number;
    towns:TownInRegion[];
    constructor(name: string, totalAds: number) {
        this.name = name;
        this.totalAds = totalAds;
    }
}
