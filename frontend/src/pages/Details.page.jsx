import React, { useEffect, useState, useContext } from "react";
import '@Styles/DetailsPage.css'
import Icons from "@/components/Details/icons";
import Detailsbloc from "@/components/Details/detailsbloc";
import { getDetailsToken } from "@/utils/api";
import { useParams } from "react-router";
import { AuthContext } from '@/context/AuthContext'

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
            {/* <div className="Container">
             <img src={details?.previewUrl} alt="" />   */}
             <Detailsbloc details={details} owner= {owner} creator={creator} isOwner={isOwner}/> 
             {console.log(details.liked)}
             <Icons id ={id} Like={details.liked} account ={account} />  
              
           
            </div> 
    )
}

export default DetailsPage;