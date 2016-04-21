(function() {
    'use strict';

    angular
        .module('volunteercrowdApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('availability', {
            parent: 'entity',
            url: '/availability?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'volunteercrowdApp.availability.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/availability/availabilities.html',
                    controller: 'AvailabilityController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('availability');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('availability-detail', {
            parent: 'entity',
            url: '/availability/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'volunteercrowdApp.availability.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/availability/availability-detail.html',
                    controller: 'AvailabilityDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('availability');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Availability', function($stateParams, Availability) {
                    return Availability.get({id : $stateParams.id});
                }]
            }
        })
        .state('availability.new', {
            parent: 'availability',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/availability/availability-dialog.html',
                    controller: 'AvailabilityDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                startMoment: null,
                                endMoment: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('availability', null, { reload: true });
                }, function() {
                    $state.go('availability');
                });
            }]
        })
        .state('availability.edit', {
            parent: 'availability',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/availability/availability-dialog.html',
                    controller: 'AvailabilityDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Availability', function(Availability) {
                            return Availability.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('availability', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('availability.delete', {
            parent: 'availability',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/availability/availability-delete-dialog.html',
                    controller: 'AvailabilityDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Availability', function(Availability) {
                            return Availability.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('availability', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
