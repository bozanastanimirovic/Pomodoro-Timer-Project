import { Role } from "./role";
import { Team } from "./team";

export class User{
    id!:number;
    username!:string;
    email!:string;
    name!:string;
    surname!:string;
    sessionCounter!:number;
    role!:Role;
    team!:Team;

    selected?: boolean = false;
}