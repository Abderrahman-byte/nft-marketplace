import React, { useEffect, useState, useContext, useCallback } from 'react'
import { useParams } from 'react-router'

import { getDetailsToken } from '@Utils/api'
import { AuthContext } from '@Context/AuthContext'
import DetailsCard from '@Components/DetailsToken/DetailsCard'
import DetailsIcons from '@Components/DetailsToken/DeatilsIcons'

import '@Styles/DetailsPage.css'

const DetailsPage = () => {
	const { id } = useParams()
	const { account, authenticated } = useContext(AuthContext)
	const [details, setdetails] = useState(undefined)

    const setPrice = (price) => setdetails(prevDetails => {
        if (!prevDetails) return prevDetails
        return {...prevDetails, price}
    })

    const setOwner = (owner) => setdetails(prevDetails => {
        if (!prevDetails) return prevDetails
        return {...prevDetails, owner: {...owner}, instantSale: false}
    })

    const setHighestBid = (price) => setdetails(prevDetails => {
        if (!prevDetails) return
        setdetails({...prevDetails, highestBid: { from : {...account}, price}})
    })

    const onPlacedBid = (price) => {
        if (!details || !details?.highestBid) return

        if (details?.highestBid?.price < price) setHighestBid(price)
    }

	const getDetails = async (id) => {
		const details = await getDetailsToken(id)
		if (details) setdetails(details)
	}

	useEffect(() => {
		getDetails(id)
	}, [id])

    if (!details) return <></>

    // console.log(details)

	return (
		<div className="DetailsPage">
           <div className="image">  
               <div className="labels">                                             
                   {/* <label className="Art"> <span> ART </span></label>                */}
                   {/* <label className="unlockable"> <span> UNLOCKABLE</span></label> */}
               </div>
               <img src={details?.previewUrl} alt="" />
           </div> 
           
            <DetailsCard 
                onPriceUpdated={setPrice} 
                details={details} 
                owner={details?.owner} 
                creator={details?.creator} 
                isOwner={authenticated && account && details && details?.owner?.id === account?.id} 
                onAcceptedCallback={setOwner}
                onPurchaseSuccess={() => setOwner(account)}
                onPlacedBid={onPlacedBid}
            />
            <DetailsIcons id ={id} Like={details.liked} account ={account}/>
        </div>
	)
}

export default DetailsPage
