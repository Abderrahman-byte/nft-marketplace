import React, { useState } from "react";

import'./styles.css'



const Icons = ()=>{
  const [heart, setheart]=useState("heart-icon")

  const changeIcon =(e)=>{
    if( heart ==='heart-icon')
    setheart("heart-fill-icon");
    
    else setheart("heart-icon")
    

  }
  return(
      <div className="Icons">
          <div className="icon icon-close">
               <i className="close-icon"> </i>
          </div>
          <div className="icon">
                 <i className="share2-icon"></i>
          </div>
          <div className="icon icon-heart" onClick={(e)=>{
            changeIcon(e)
          }}>
                <i className={heart}> </i>
          </div>
          <div className="icon ">
               <i className="more2-icon"></i>
          </div>
      </div>
  )

}

export default Icons;