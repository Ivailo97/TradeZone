import { TownInRegion } from "./town-in-region";

export class Region {
    name: string;
    totalAds: number;
    totalUsers: number;
    towns: TownInRegion[];
    constructor(name: string, totalAds: number, totalUsers: number) {
        this.name = name;
        this.totalAds = totalAds;
        this.totalUsers = totalUsers;
    }
}
