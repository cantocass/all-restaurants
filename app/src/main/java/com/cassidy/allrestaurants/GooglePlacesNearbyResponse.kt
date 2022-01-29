package com.cassidy.allrestaurants

import com.google.gson.annotations.SerializedName

data class GooglePlacesNearbyResponse(

	@field:SerializedName("html_attributions")
	val htmlAttributions: List<Any?>,

	@field:SerializedName("results")
	val results: List<Place>,

	@field:SerializedName("status")
	val status: String,

	@field:SerializedName("next_page_token")
	val nextPageToken: String? = null,
)

data class Geometry(

	@field:SerializedName("viewport")
	val viewport: Bounds,

	@field:SerializedName("location")
	val location: LatLngLiteral
)

data class Bounds (

	@field:SerializedName("southwest")
	val southwest: LatLngLiteral,

	@field:SerializedName("northeast")
	val northeast: LatLngLiteral
)

data class LatLngLiteral(

	@field:SerializedName("lng")
	val lng: Double? = null,

	@field:SerializedName("lat")
	val lat: Double? = null
)

data class PlusCode(

	@field:SerializedName("compound_code")
	val compoundCode: String? = null,

	@field:SerializedName("global_code")
	val globalCode: String
)

data class PlacePhoto(

	@field:SerializedName("photo_reference")
	val photoReference: String,

	@field:SerializedName("width")
	val width: Int,

	@field:SerializedName("html_attributions")
	val htmlAttributions: List<String?>,

	@field:SerializedName("height")
	val height: Int?
)

data class PlaceOpeningHours(

	@field:SerializedName("open_now")
	val openNow: Boolean? = null
)

data class Place(

	@field:SerializedName("types")
	val types: List<String>? = null,

	@field:SerializedName("business_status")
	val businessStatus: String? = null,

	@field:SerializedName("icon")
	val icon: String? = null,

	@field:SerializedName("rating")
	val rating: Double? = null,

	@field:SerializedName("icon_background_color")
	val iconBackgroundColor: String? = null,

	@field:SerializedName("photos")
	val photos: List<PlacePhoto>? = null,

	@field:SerializedName("reference")
	val reference: String? = null,

	@field:SerializedName("user_ratings_total")
	val userRatingsTotal: Int? = null,

	@field:SerializedName("price_level")
	val priceLevel: Int? = null,

	@field:SerializedName("scope")
	val scope: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("opening_hours")
	val openingHours: PlaceOpeningHours? = null,

	@field:SerializedName("geometry")
	val geometry: Geometry? = null,

	@field:SerializedName("icon_mask_base_uri")
	val iconMaskBaseUri: String? = null,

	@field:SerializedName("vicinity")
	val vicinity: String? = null,

	@field:SerializedName("plus_code")
	val plusCode: PlusCode? = null,

	@field:SerializedName("place_id")
	val placeId: String? = null,

	@field:SerializedName("permanently_closed")
	val permanentlyClosed: Boolean? = null
)
