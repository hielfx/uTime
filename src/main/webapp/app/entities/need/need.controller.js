(function() {
    'use strict';

    angular
        .module('volunteercrowdApp')
        .controller('NeedController', NeedController);

    NeedController.$inject = ['$scope', '$state', 'Need', 'NeedSearch', 'AppUserNeed', 'AppUserNeedSearch', 'ParseLinks', 'AlertService', 'pagingParams', 'paginationConstants'];

    function NeedController ($scope, $state, Need, NeedSearch, AppUserNeed, AppUserNeedSearch, ParseLinks, AlertService, pagingParams, paginationConstants) {

        var useAppUser = $state.current.data.useAppUser; //Check if we have to use the appUserNeed service

        var vm = this;
        vm.loadAll = loadAll;
        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.clear = clear;
        vm.search = search;
        vm.searchQuery = pagingParams.search;
        vm.currentSearch = pagingParams.search;
        vm.loadAll();

        vm.useAppUser = useAppUser; //To load it to the view
        $scope.useAppUser = useAppUser;

        function loadAll () {
            if (pagingParams.search) {
                if(useAppUser!=true) {
                    NeedSearch.query({
                        query: pagingParams.search,
                        page: pagingParams.page - 1,
                        size: paginationConstants.itemsPerPage,
                        sort: sort()
                    }, onSuccess, onError);
                }else{
                    AppUserNeedSearch.query({
                        query: pagingParams.search,
                        page: pagingParams.page - 1,
                        size: paginationConstants.itemsPerPage,
                        sort: sort()
                    }, onSuccess, onError);
                }
            } else {
                if(useAppUser!=true) {
                    Need.query({
                        page: pagingParams.page - 1,
                        size: paginationConstants.itemsPerPage,
                        sort: sort()
                    }, onSuccess, onError);
                }else{
                    AppUserNeed.query({
                        page: pagingParams.page - 1,
                        size: paginationConstants.itemsPerPage,
                        sort: sort()
                    }, onSuccess, onError);
                }
            }
            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }
            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                vm.needs = data;
                vm.page = pagingParams.page;
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function loadPage (page) {
            vm.page = page;
            vm.transition();
        }

        function transition () {
            $state.transitionTo($state.$current, {
                page: vm.page,
                sort: vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc'),
                search: vm.currentSearch
            });
        }

        function search (searchQuery) {
            if (!searchQuery){
                return vm.clear();
            }
            vm.links = null;
            vm.page = 1;
            vm.predicate = '_score';
            vm.reverse = false;
            vm.currentSearch = searchQuery;
            vm.transition();
        }

        function clear () {
            vm.links = null;
            vm.page = 1;
            vm.predicate = 'id';
            vm.reverse = true;
            vm.currentSearch = null;
            vm.transition();
        }

    }

})();
