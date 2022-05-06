import React from "react";

import Detailscard from "./detailscard";
import FixedBox from "./fixedbox";

import'./styles.css'
/**fix the fixedBox */

const Detailsbloc = ( {details, owner, creator, isOwner})=>{
    return(
        <div className="detailsbloc">
            <div className="img-01" style={{ 'backgroundImage': `url(${details?.previewUrl})`}}>
               <div className="labels">                                             
                   <label className="Art"> <span> ART </span></label>               
                   <label className="unlockable"> <span> UNLOCKABLE</span></label>
               </div>
             
            </div>
            <div className="frame-971">
            <Detailscard details = {details} owner ={owner} creator={creator}/>
            
            {!isOwner? (<FixedBox />):null}
            </div>
        </div>  
    )
}
export default Detailsbloc;