import { SessionType } from "./enums/sessionType";
import { User } from "./user";

export class Session{
    id!:number;
    timeLeft!:number;
    sesssionType!:SessionType;
    isPaused!:boolean;
    isFinished!:boolean;
    date!:Date;
    user!:User;
}