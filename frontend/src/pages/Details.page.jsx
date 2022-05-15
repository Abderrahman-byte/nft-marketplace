import React, { useEffect, useState, useContext } from "react";
import { useParams } from "react-router";

import Icons from "@Components/Details/icons";
import Detailsbloc from "@Components/Details/detailsbloc";
import { getDetailsToken } from "@Utils/api";
import { AuthContext } from '@Context/AuthContext'

import '@Styles/DetailsPage.css'

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
    },[id])

    return (
        <div className="DetailsPage">
             <Detailsbloc details={details} owner= {owner} creator={creator} isOwner={isOwner}/> 
             <Icons id ={id} Like={details.liked} account ={account} />    
        </div>
    )
}

export default DetailsPage;