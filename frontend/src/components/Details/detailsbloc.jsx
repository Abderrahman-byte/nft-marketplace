import React, { useEffect, useState } from "react";

import Detailscard from "./detailscard";
import FixedBox from "./fixedbox";

import'./styles.css'

const Detailsbloc = ( {details, owner, creator})=>{
  
    return(
        <div className="detailsbloc">
            <div className="img-01" style={{ 'backgroundImage': `url(${details.previewUrl})`}}>
               <div className="labels">                                             
                   <label className="Art"> <span> ART </span></label>               
                   <label className="unlockable"> <span> UNLOCKABLE</span></label>
               </div>
             
            </div>
            <div className="frame-971">
            <Detailscard details = {details} owner ={owner} creator={creator}/>
          
            <FixedBox />
            </div>
        </div>
        
    )
}

export default Detailsbloc;