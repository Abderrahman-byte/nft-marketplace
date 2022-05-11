import React, { useContext } from "react";
import { AuthContext } from "@/context/AuthContext";
import { buildApiUrl, createTransaction } from "@/utils/api";
import LoadingCard from "../LoadingCard";
import MessageCard from '../MessageCard';
import LoginPage from "@/pages/Login.page";
import QrCodeCard from "../QrCodeCard";


const PurchaseNowBtn=({tokenId, accountFrom})=>{
//connect wallet
const {authenticated, account, openModel, closeModel} = useContext(AuthContext)

const ShowError = (msg) =>{
 
    openModel(<MessageCard title='' text={msg} closeBtnCallback={closeModel} />, closeModel)
}
const PurchaseNow = async()=>{
    
    const closeModelQr = ()=>{
        if(event.readyState !== event.CLOSED) event.close()
        closeModel()
    }

    if(!authenticated || !account ) return <LoginPage/>
    openModel(<LoadingCard/>)
    const[ref, error] = await createTransaction({tokenId, accountFrom})
    if(ref === null ) return ShowError(error?.detail)
    const event = new EventSource(buildApiUrl('/marketplace/buy') + `?ref=${ref}`, { withCredentials : true })


    event.addEventListener('qr', (eventqr)=>{
        const dataForqr = eventqr.data
        dataForqr ? openModel(<QrCodeCard 
            title='Scan to confirm' 
            text='Confirm by scanning this code' 
            value={dataForqr} 
            closeBtnCallback={closeModelQr} />, closeModelQr) : console.log('no Data found for the qr');
    })

    event.addEventListener('accepted', ()=>{
        openModel(<MessageCard title='Success' text='The transaction is done successfully' closeBtnCallback={closeModel} />, closeModel)

        if(event.readyState !== event.CLOSED) event.close()
    },[tokenId, accountFrom, authenticated, account])

    event.addEventListener('error', eventqr => {
        const error = JSON.parse(eventqr.data || '{}')

        if(event.readyState !== event.CLOSED) event.close()

        ShowError(error.detail)
    })
    
}
return (
   
    <div>
        
    <button onClick={PurchaseNow} className="btn btn-blue">
                      <label className="left" > Purchase now</label>
     </button>
  </div>
)

}
export default PurchaseNowBtn;