app.controller("voucher-ctrl",function($scope,$http){
	$scope.items=[];
	$scope.products = [];
	$scope.users = [];
	$scope.form={};

	$scope.initialize = function(){
		$http.get("/rest/vouchers").then(resp =>{
			$scope.items = resp.data;
			$scope.items.forEach(item =>{
				item.createDate = new Date(item.createDate)
			})
		});
		$http.get("/rest/products").then(resp =>{
			$scope.products = resp.data;
		});
		$http.get("/rest/accounts").then(resp =>{
			$scope.users = resp.data;
		});
	}
	$scope.initialize();

	$scope.reset = function(){
		$scope.form={
				
				
		};
	}
	$scope.edit = function(item){
		$scope.form = angular.copy(item);
		$(".nav-tabs button:eq(0)").tab('show')
	}
	$scope.random = function(){
		let r = Math.random().toString(36).substring(7);
		$scope.form.voucherCode = r;
		
	}
	$scope.create = function(){
		var item = angular.copy($scope.form);
		if($scope.form.voucherCode == null){
			alert("VoucherCode không được bỏ trống");
		}
		else if($scope.form.maxUser == null){
			alert("Max User không được bỏ trống");
		}else if($scope.form.discountAmount == null){
			alert("DiscountAmout không được bỏ trống");
		}else if($scope.form.status == null){
			alert("Vui lòng chọn status");
		}else if($scope.form.startDate == null){
			alert("Vui lòng chọn StartDate");
		}else if($scope.form.endDate == null){
			alert("Vui lòng chọn EndDate");
		}else if($scope.form.product == null){
			alert("Vui lòng chọn Product");
		}else if($scope.form.user == null){
			alert("Vui lòng chọn User");
		}else{
			$http.post(`/rest/vouchers`,item).then(resp =>{
			
			$scope.items.push(resp.data);
			$scope.reset();
			alert("Thêm mới voucher thành công");
		}).catch(error =>{
			alert("Lổi thêm mới voucher");
			console.log("Error",error);
		});
		}
		
	}
	$scope.update = function(){
		var item = angular.copy($scope.form);
		if($scope.form.voucherCode == ''){
			alert("VoucherCode không được bỏ trống");
		}
		else if($scope.form.maxUser == null){
			alert("Max User không được bỏ trống");
		}else if($scope.form.discountAmount == null){
			alert("DiscountAmout không được bỏ trống");
		}else{
			$http.put(`/rest/vouchers/${item.id}`,item).then(resp=>{
			var index = $scope.items.findIndex(p => p.id == item.id);
			$scope.items[index] = item;
			alert("Cập nhật thành công");
		}).catch(error=>{
			alert("Cập nhật thất bại");
			console.log("Error",error);
		});
		}
		
	}
	
	
	$scope.delete = function(item){
		$http.delete(`/rest/vouchers/${item.id}`).then(resp=>{
			var index = $scope.items.findIndex(p => p.id == item.id);
			$scope.items.splice(index,1);
			$scope.reset();
			alert("Xóa thành công");
		}).catch(error=>{
			alert("Xóa thất bại");
			console.log("Error",error);
		});
	}
	
	
	
	$scope.pager = {
		page:0,
		size: 10,
		get items(){
			var start = this.page * this.size;
			return $scope.items.slice(start,start + this.size);
		},
		get count(){
			return Math.ceil(1.0 * $scope.items.length/ this.size);
		},
		first(){
			this.page = 0;
		},
		prev(){
			this.page--;
			if(this.page <0){
				this.last();
			}
		},
		next(){
			this.page++;
			if(this.page >= this.count){
				this.first();
			}
		},
		last(){
			this.page = this.count-1;
		}
	}
});