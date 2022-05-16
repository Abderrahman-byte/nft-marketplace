import React, { useEffect, useState, useContext } from "react";
import { useParams } from "react-router";

import { getDetailsToken } from "@Utils/api";
import { AuthContext } from '@Context/AuthContext'

import '@Styles/DetailsPage.css'

import DetailsCard from "@/components/DetailsToken/DetailsCard";
import DetailsIcons from "@/components/DetailsToken/DeatilsIcons";


const DetailsPage = () => {
    const { id } = useParams()
    const { account } = useContext(AuthContext)
    const [details, setdetails]= useState({});
    const [owner, setowner] = useState({})
    const [creator, setcreator]=useState({})
    const[isOwner, setIsOwner] =useState(false)

    const getDetails = async (id) => {    
        const [details, owner, creator] = await getDetailsToken(id)
        setdetails(details)                                           
        setowner(owner)                                              
        setcreator(creator)                                         
       if(owner.id === account.id)                                  
          setIsOwner(true)                                          
    }                                                               

    useEffect(()=>{    
        getDetails(id);
        
    },[id, account])

    return (
        
        <div className="DetailsPage">
           <div className="image">  
               <div className="labels">                                             
                   <label className="Art"> <span> ART </span></label>               
                   <label className="unlockable"> <span> UNLOCKABLE</span></label>
               </div>
               <img src={details?.previewUrl} alt="" />
           </div> 
           
             <DetailsCard details={details} owner= {owner} creator={creator} isOwner={isOwner} />
             <DetailsIcons id ={id} Like={details.liked} account ={account}/>
        </div>
    )
}

export default DetailsPage;