import React, {useEffect, useState} from "react";
import './styles.css'
import { getBidsToken } from "@/utils/api";
import BidsDiv from "./Bidsdiv";
const DetailsBid=({Id})=>{
    const [bids, setBids]= useState([])
    const [page, setPage] = useState(1)
    const [isMore, setMore] = useState(true)
    const Bidscalled = 3

    const getBidsForToken = async ()=>{
        if(page <=0 || !isMore) return
        const result = await getBidsToken(Id, Bidscalled, (page - 1) * Bidscalled)
        setBids([...bids, ...result])
        if (result.length < Bidscalled) setMore(false)
    }

   
    useEffect (()=>{
    
        getBidsForToken()
    }, [page])
    
    useEffect(()=>{
        setBids([])
       setPage(0)
        setMore(true)
       setTimeout(() => setPage(1), 0)
    },[Id, getBidsToken])

    const More=()=>{
        if(isMore)
        {
            setPage(page + 1)
        }else return
    }
    return(
        
        <div className="bids-cont">
        <div className="bids-scroll"  onScroll={()=> More()} >
             {bids.map((bid, i)=> <BidsDiv key ={i} {...bid}/>)}
         </div>
         </div>
    )

}
export default DetailsBid;