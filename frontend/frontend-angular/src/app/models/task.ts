import { User } from "./user";
import { Team } from "./team";

export class Task{
    id!:number;
    name!:string;
    sessionsCompleted!:number;
    finished!:boolean;
    user!:User;
    team!:Team;
}