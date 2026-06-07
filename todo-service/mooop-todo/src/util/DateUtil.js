
import { 
    format 
    , addDays 
    , subMonths 
} from 'date-fns';



export const dateUtil = {
    today:()=>{
        const today = new Date();
        return format(today , 'yyyy-MM-dd');
    }

}