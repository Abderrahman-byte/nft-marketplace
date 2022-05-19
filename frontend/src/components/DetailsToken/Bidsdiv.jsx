import React,{useContext, useEffect, useState} from "react"; 
import './styles.css'
import { AuthContext } from '@Context/AuthContext'
import { respondOffer } from "@Utils/api";





const BidsDiv = ({id,from,response, price, owner, onAcceptedCallback}) => {
    const { account } = useContext(AuthContext)
    const [localResponse, setLocalResponse] = useState(response)
    
    const sendResponse=(action)=>{
        const data={
            action : action
        }
       handlerResponse(data)

    }
    const handlerResponse = async (data) => {
        const [done, error] = await respondOffer(id, data)
        
        if (data.action === 'reject' && done) setLocalResponse('REJECTED')
        if (data.action === 'accept' && done) {
            setLocalResponse('ACCEPTED')
            if (typeof onAcceptedCallback === 'function') onAcceptedCallback(from)
        }
    }

      const renderButtons=()=>{
         if((account.id === owner.id)  && !(account.id === from.id) ) 
         {
             if(localResponse === "PENDING"){
             return(
                <div className="Accept-Reject">
                <button onClick={()=>sendResponse('accept')}  className="btn-accept">
                    Accept
                </button>
                <button onClick={()=>sendResponse('reject')} className="btn-deny">
                    Reject     
                </button>
                 </div>
             )
             }
              else if((localResponse == "REJECTED"))
             {
                 return <div className="REJECTED"> <span> This bid has been rejected </span> </div>
             }
         }
        
      }

    useEffect(() => {
        if (response) setLocalResponse(response)
    }, [response])
      

    return (


        <div className="DetailsInfo ">

                <div className="frame-966">
                    <div className="avatar-1">
                        <img src={from.avatarUrl} alt="" />
                    </div>
                    <div className="infos-1">
                        <div className="frame-966">
                        <span className="type">
                            Bids 
                        </span>
                        <span className="price">
                        {price} RVN
                        </span>
                        <span className="type">
                           by 
                        </span>
                        </div>
                        <span className="FullName">
                           {from?.displayName || from?.username} 
                        </span>
                    </div>   
                </div>
               {renderButtons()}
                <div className="horizontal-divider "></div>
        </div>
          

    )

}

export default BidsDiv;