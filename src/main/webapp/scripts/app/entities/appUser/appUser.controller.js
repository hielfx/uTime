'use strict';

angular.module('volunteercrowdApp')
    .controller('AppUserController', function ($scope, $state, AppUser, AppUserSearch, ParseLinks) {

        $scope.appUsers = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.page = 1;
        $scope.loadAll = function() {
            AppUser.query({page: $scope.page - 1, size: 20, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.totalItems = headers('X-Total-Count');
                $scope.appUsers = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.search = function () {
            AppUserSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.appUsers = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.appUser = {
                phoneNumber: null,
                isOnline: null,
                id: null
            };
        };
    });
