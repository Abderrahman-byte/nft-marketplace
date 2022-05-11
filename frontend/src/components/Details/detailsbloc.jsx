import React from "react";

import Detailscard from "./detailscard";
import FixedBox from "./fixedbox";

import'./styles.css'
/**fix the fixedBox */
/*test is the token is for sell*/
const Detailsbloc = ( {details, owner, creator, isOwner})=>{
    return(
        <div className="detailsbloc"> 
            <div className="img-01" >
            <img src={details?.previewUrl} alt="" />
               <div className="labels">                                             
                   <label className="Art"> <span> ART </span></label>               
                   <label className="unlockable"> <span> UNLOCKABLE</span></label>
               </div>
            </div>
            <div className="frame-971">
            <Detailscard details = {details} owner ={owner} creator={creator}/>
            {!isOwner? (<FixedBox details = {details} owner ={owner} creator={creator}/>):null}
            </div>
        </div>  
    )
}
export default Detailsbloc;