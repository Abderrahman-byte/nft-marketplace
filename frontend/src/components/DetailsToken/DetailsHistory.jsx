import React, {useContext, useEffect, useState} from "react";
import './styles.css'
import { getHistoryTransaction } from "@/utils/api";
import HistoDiv from "./Histodiv";
const DetailsHistory=({Id})=>{

    const [transaction, setTransaction]= useState([])
    const [page, setPage] = useState(1)
    const [isMore, setMore] = useState(true)
    const transshwonFirst = 3

    const getTransaction = async ()=>{
        if(page <=0 || !isMore) return
        const result = await getHistoryTransaction(Id, transshwonFirst, (page-1)*transshwonFirst)
        setTransaction([...transaction, ...result])
        if (result.length < transshwonFirst) setMore(false)
    }

    useEffect(()=>{
        setTransaction([])
        setPage(0)
        setMore(true)

        setTimeout(() => setPage(1), 0)

    },[Id, getHistoryTransaction])
    useEffect (()=>{
        getTransaction()
    }, [page])
    const More=()=>{
        if(isMore)
        {
            setPage(page+1)
        }else return
    }
    return(
        
        <div className="bids-cont">
        <div className="bids-scroll" onScroll={()=> More()} >
             {transaction.map((bid, i)=><HistoDiv key ={i} {...bid}/>)}
         </div>
         </div>
    )

}
export default DetailsHistory;