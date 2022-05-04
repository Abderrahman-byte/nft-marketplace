import { AuthContext } from '@/context/AuthContext'
import { createCollection, getUserCollections } from '@/utils/api'
import { DEFAULT_ERROR, translateError } from '@/utils/generic'
import React, { useCallback, useContext, useEffect, useState } from 'react'
import CreateCollectionForm from '../CreateCollectionForm'
import LoadingCard from '../LoadingCard'

const ChooseCollectionInput = ({ onSelectedCallback }) => {
	const { openModel, closeModel } = useContext(AuthContext)
	const [collections, setCollections] = useState([])

	const fetchCollections = async () => {
		openModel(<LoadingCard />)

		const collectionsList = await getUserCollections()
		setCollections(collectionsList)

		closeModel()
	}

	const collectionSelected = (id) => {
		setCollections(
			collections.map((collection) => {
				if (collection.id === id && typeof onSelectedCallback === 'function') {
					onSelectedCallback(collection.selected ? null : collection)	
				}

				collection.selected = collection.id === id && !collection.selected

				return collection
			})
		)
	}

	const generateCollectionsElts = useCallback(() => {
		return collections.map((coll, i) => (
			<div
				key={i}
				className={`collection ${coll.selected ? 'selected' : ''}`}
				onClick={() => collectionSelected(coll.id)}
			>
				{coll.imageUrl ? (
					<img src={coll.imageUrl} className='collection-image' />
				) : (
					<span className='image-placeholder'></span>
				)}

				<span className='title'>{coll.name}</span>
			</div>
		))
	}, [collections])

	const submitCreateCollection = async (formData, collectionData) => {
		openModel(<LoadingCard />)

		const [data, error] = await createCollection(formData)

		if (!data && error) {
			const errors = translateError(error)
			openModel(
				<CreateCollectionForm
					defaultImage={formData.get('image')}
					defaultData={collectionData}
					defaultErrors={errors}
					onSubmitCallback={submitCreateCollection}
				/>,
				closeModel
			)
		} else if (data) {
			setCollections([{ ...data, ...collectionData}, ...collections])
			closeModel()
		} else {
			openModel(
				<CreateCollectionForm
					defaultImage={formData.get('image')}
					defaultData={collectionData}
					defaultErrors={[DEFAULT_ERROR]}
					onSubmitCallback={submitCreateCollection}
				/>,
				closeModel
			)
		}
	}

	const CreateCollectionClickCallback = () => {
		openModel(
			<CreateCollectionForm onSubmitCallback={submitCreateCollection} />,
			closeModel
		)
	}

	useEffect(() => {
		fetchCollections()
	}, [])

	return (
		<div className='ChooseCollectionInput'>
			<label>Choose collection</label>
			<small>Choose an exiting collection or create a new one</small>

			<div className='collections-list'>
				<div
					className='collection add-collection-btn'
					onClick={CreateCollectionClickCallback}
				>
					<span className='image-placeholder'>
						<i className='plus-icon'></i>
					</span>
					<span className='title'>Create collection</span>
				</div>
				{generateCollectionsElts()}
			</div>
		</div>
	)
}

export default ChooseCollectionInput
