import React, { useEffect, useState } from "react";
import '@Styles/DetailsPage.css'
import Icons from "@/components/Details/icons";
import Detailsbloc from "@/components/Details/detailsbloc";
import { getDetailsToken } from "@/utils/api";
import { useParams } from "react-router";

const DetailsPage = () => {
    const { id } = useParams()

    const [details, setdetails]= useState({});
    const [owner, setowner] = useState({})
    const [creator, setcreator]=useState({})

    const getDetails = async (id) => { 
              
        const [details, owner, creator] = await getDetailsToken(id)
     
        setdetails(details)
        setowner(owner)
        setcreator(creator)
       console.log(details)
       console.log(owner)
     
    }

    useEffect(()=>{
        
        getDetails(id);
    },[id])


    return (
        <div className="DetailsPage">
             <Detailsbloc details={details} owner= {owner} creator={creator}/>
             <Icons/>
             
        </div>
    )
}

export default DetailsPage;